package com.zzx.executor.core.report;

import com.alibaba.fastjson.JSONObject;
import com.zzx.executor.dao.TradeEntityMapper;
import com.zzx.executor.entity.ReOrderEntity;
import com.zzx.executor.entity.TradeEntity;
import com.zzx.executor.util.ExecutorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
@EnableScheduling
public class Report implements Runnable {

    @Autowired
    TradeEntityMapper mapper;
    @Autowired
    Report report;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExecutorService threadPool;

    @Lazy(false)
    @Scheduled(cron = "*/59 * * * * ?")
    public void report() {
        System.out.println("进入上报");
        threadPool.submit(report);
    }

    @Override
    public void run() {
        List<ReOrderEntity> list = get();
        for (ReOrderEntity record : list) {
            httpReport(record);
        }
    }

    private List<ReOrderEntity> get() {
        List<ReOrderEntity> list = mapper.rereport(gettime());
        return list;
    }

    private void httpReport(ReOrderEntity record) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        JSONObject params = new JSONObject();
        params.put("order", record.getBank_account());
        params.put("sbOrder", record.getBank());
        params.put("money", record.getMoney());
        params.put("orderTime", record.getTrade_date() + " " + record.getTrade_time());
        params.put("type", record.getTrade_type());
        params.put("mark", record.getRemark());
        params.put("signkey", "123456");
        params.put("chanel", "taobao");
        params.put("business", "business");
        params.put("result", "0");
        params.put("sign", "f06a62b8bb7f72ee9c9e5a4b4a02028d");

        System.out.println("数据：" + params);
        HttpEntity<String> requestEntity =
                new HttpEntity<>(params.toJSONString(), headers);
        ResponseEntity<String> response = null;
        try {
//          response = restTemplate.exchange("http://www.xzpayhome.com/Pay_Tbdf_notifyurl.html", HttpMethod.POST,
            response = restTemplate.exchange("http://120.24.176.141:8789/tbdf/notify", HttpMethod.POST,
                    requestEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("**************补单上报*************");
        System.out.println("补单数据：" + params);
        if (response != null && response.getBody().contains("success")) {
            mapper.upda(record.getRemark());
        }
    }


    public static String gettime() {
        Date date = new Date();
        long times = date.getTime();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(times);
        return time;
    }


}
