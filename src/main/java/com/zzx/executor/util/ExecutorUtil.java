package com.zzx.executor.util;

import com.zzx.executor.entity.TradeEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExecutorUtil {

    public static boolean doPrepare(String path) {
        boolean flag = false;
        ClassPathResource classPathResource = new ClassPathResource("tool/QuarkGG.exe");
        String filename = classPathResource.getFilename();

        try {
            InputStream inputStream = classPathResource.getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            String fileName = new String(filename.getBytes("UTF-8"), "iso-8859-1");
            String filePath = path + File.separator + fileName;
            File file = new File(filePath);
            if (!file.exists()){
                FileOutputStream output = new FileOutputStream(file);
                output.write(bytes);
            }
            flag = true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return flag;
    }

    public static String getRecordHash(TradeEntity record){
        return record.getTradeDate()+record.getTradeTime()+record.getMoney()+record.getBankAccount();
    }

    public static String md5(String str){
        StringBuilder sb = new StringBuilder();
        try {
            //获取MD5加密器
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes();
            byte[] digest = md.digest(bytes);

            for (byte b : digest) {
                //把每个字节转换成16进制数
                int d = b & 0xff;//0x00 00 00 00 ff
                String hexString = Integer.toHexString(d);
                if(hexString.length() == 1){
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return String.valueOf(sb);
    }
}
