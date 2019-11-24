package com.zzx.executor.core;

import com.zzx.executor.dao.TradeEntityMapper;
import com.zzx.executor.entity.TradeEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class XMLResolver extends AbstractXMLResolver {

    public static String name="";

    @Autowired
    TradeEntityMapper mapper;


    @Override
    public List<TradeEntity> messageResolve(String xmlData) throws InterruptedException {

        Pattern pattern = Pattern.compile("(.*?)<spandata-reactid=.0.5.2></span>(?<datanum>.*)<divclass=row-mod__row___30Zj1style=margin-bottom:10px;data-reactid=.0.5.4>(.*?)");
        Matcher matcher = pattern.matcher(xmlData);
        String datanum = null;
        if (matcher.find()) {
            datanum = matcher.group("datanum");
            System.out.println(datanum);
        }
        String result = null;
        if (datanum != null) {

            Thread.sleep(500);

            result = datanum;
            //TODO 数据解析中心
            result = result.replaceAll("&nbsp;", "");
            result=result.replaceAll("<div(.*?)>","<div>");
            result=result.replaceAll("<tableclass(.*?)>","");
            result=result.replaceAll("<colgroupdata-reactid(.*?)</colgroup>","");
            result=result.replaceAll("<tbodydata-reactid(.*?)>","");
            result=result.replaceAll("<trdata-reactid(.*?)>","");
            result=result.replaceAll("<tdclass(.*?)>","");
            result=result.replaceAll("<labelclass(.*?)>","");
            result=result.replaceAll("<inputtype(.*?)>","");
            result=result.replaceAll("<span(.*?)>","<span>");

//            result=result.replaceAll("</td><div><pstyle(.*?)</span></a></span></span></p></div></td><div><pstyle=(.*?)<span>","<u>");
//            result=result.replaceAll("</span></p><div><pstyle=(.*?)</p></div></div></td><div>","</u>");
//            result=result.replaceAll("</p></div></td><div><pdata-reactid(.*?)</td></tr></tbody></table></div>","</div>");
//            result=result.replaceAll("</label><span><pdata-reactid(.*?)>","");
//            result=result.replaceAll("<span></span>","");
//            result=result.replaceAll("<div>","<tr>");
//            result=result.replaceAll("</div>","</tr>");
//            result=result.replaceAll("<span>","<td>");
//            result=result.replaceAll("</span>","</td>");
//            result=result.replaceAll("<img(.*?>)","");
//            result=result.replaceAll("</label><u>","<td>");
//            result=result.replaceAll("</u><tr><pdata-reactid(.*?)<td>","</td><td>");
//            result=result.replaceAll("</strong></p></tr><pstyle(.*?>)<tr></tr></td></tr></tbody></table></tr>","</tr>");
//            result=result.replaceAll("</strong></p></tr>(.*?)</tr></td></tr></tbody></table>","");
            result=result.replaceAll("</span></label></td><div><pstyle(.*?)</span></a></span></span></p></div></td><div><pstyle","<zzx>");
            result=result.replaceAll("</span></p><div><pstyle(.*?)￥","</span><span>￥");
            result=result.replaceAll("</span></strong></p></div><pstyle(.*?)</div></td></tr></tbody></table></div>","</span></div>");
            result=result.replaceAll("<span></span>","");
            result=result.replaceAll("<zzx>(.*?)<span>","</span><span>");
            result=result.replaceAll("span","td");
            result=result.replaceAll("div","tr");
            result=result.replaceAll("<imgsrc(.*?)>","");



            result = "<div>" + result + "</div>";

        }
        if (result == null) {
            return new ArrayList<>();
        }


        try {
            Document document = DocumentHelper.parseText(result);
            org.dom4j.Element root = document.getRootElement();
            List<TradeEntity> records = new ArrayList<>();
            Iterator var = root.elementIterator();//xml形态的记录列表
//            int i = 0;

            while (var.hasNext()) {
                TradeEntity record = new TradeEntity();
                org.dom4j.Element recordRoot = (org.dom4j.Element) var.next();
                Iterator var2 = recordRoot.elementIterator();

                if (var2.hasNext()) {
                    var2.next();
                }
                if (var2.hasNext()) {
                    String item6 = ((org.dom4j.Element) var2.next()).getText();
                    record.setBankAccount(item6);
                    record.setTradeType(item6);
                }

                if (var2.hasNext()) {
                    var2.next();
                }
                // 时间
                if (var2.hasNext()) {
                    String item2 = ((org.dom4j.Element) var2.next()).getText();
                    String data=item2.substring(0,10);
                    String time=item2.substring(10);
                    record.setTradeDate(data);
                    record.setTradeTime(time);
                }

                if (var2.hasNext()) {
                    String item2 = ((org.dom4j.Element) var2.next()).getText();
                    record.setRemark(item2);
                }
                if (var2.hasNext()) {
                    var2.next();
                }

                //交易金额，带"-"的支出不要
                if (var2.hasNext()) {
                    String item4 = ((org.dom4j.Element) var2.next()).getText();
                    if (StringUtils.isEmpty(item4) || item4.contains("-")) {
                        continue; //下一行
                    }
                    item4 = item4.replace("+", "");
                    record.setMoney(item4);
                }


                record.setBank("淘宝商家");   //账号后4位

                record.setReport(false);  //已上报=false
                System.out.println("************:" + record.toString() + "************");
                if(mapper.getCount(record.getBankAccount())==1){
                    if(!mapper.getRemark(record.getBankAccount()).equals(record.getRemark())){
                        mapper.updateRemark(record.getRemark(),record.getBankAccount());
                    }
                }
                //如果这条信息已存在，说明后面的都已经解析过，不再解析
                if(record.getMoney().equals("")||record.getMoney()==null){
                    break;
                }
//                if (dataProcessor.containsRecord(record)) {
//                    break;
//                }
                records.add(record);
            }

            return records;
        } catch (DocumentException e) {

            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
