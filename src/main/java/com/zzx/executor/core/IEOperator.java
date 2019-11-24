package com.zzx.executor.core;


import com.zzx.executor.core.interfaces.IButton;
import com.zzx.executor.core.moudle_strategy.StepFore;
import com.zzx.executor.core.moudle_strategy.StepOne;
import com.zzx.executor.core.moudle_strategy.StepThree;
import com.zzx.executor.core.moudle_strategy.StepTwo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class IEOperator implements Runnable {
    private boolean notInterrupted = true;
    private long count=0;
    public WebDriver driver;
    private static String path = "C:\\Program Files";
    private static boolean firstTime = true;
    private long budan=1;
    private boolean ifonce=true;
    private boolean SYMBLE=true;


    @Autowired
    private XMLResolver task;

    @Autowired
    private ExecutorService threadPool;

    @Autowired
    private DataProcessor processor;

    @Autowired
    private IButton button;

    @Autowired
    private UI ui;

    @Autowired
    StepOne one;

    @Autowired
    StepTwo two;

    @Autowired
    StepThree three;

    @Autowired
    StepFore fore;
    public void openAndLogin() {


        if (StringUtils.isEmpty(path)) {
            System.out.println("没有找到Quark");
            return;
        }
//        System.setProperty("webdriver.ie.driver", path + File.separator + "Quark.exe");
        System.setProperty("webdriver.chrome.driver", path + File.separator + "QuarkGG.exe");
//        driver = new InternetExplorerDriver();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(button.getLoginPageURL());
    }

    @Override
    public void run() {
        try {
            this.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.notInterrupted = false;
    }

    public void start() throws InterruptedException {
        notInterrupted = true;
        processor.initParams(); //初始化上报地址、扫描周期

        if (driver == null) {
            return;
        }

        //TODO 附加参数
//        if (firstTime) {
////            driver.navigate().to("https://consumeprod.alipay.com/record/standard.htm");
////            driver.navigate().refresh();
//            Thread.sleep(2000);
//            String data = driver.getPageSource();
//            data = data.replaceAll(" ", "");
//            data = data.replaceAll("\n", "");
//            Pattern pattern = Pattern.compile("(.*?)J-userInfo-account-userEmail(.*?)(?<nameone>\\d+)(?<nametwo>.*?)</a>");
//            Matcher matcher = pattern.matcher(data);
//            while (matcher.find()) {
//                String nameone = matcher.group("nameone");
//                String nametwo = matcher.group("nametwo");
//                String name=nameone+nametwo;
//                if (!StringUtils.isEmpty(name)) {
//                    ui.showName(name);
//                    XMLResolver.name=name;
//                    firstTime = false;
//                    break;
//                }
//            }
//        }


//        driver.navigate().to("https://myseller.taobao.com/home.htm");
//        driver.findElement(By.partialLinkText("千牛卖家中心")).click();
        Thread.sleep(1000);
        driver.findElement(By.partialLinkText("待发货")).click();
        //TODO 窗口切换
        if(ifonce) {
            System.out.println("URL1：" + driver.getCurrentUrl());
            String url1=driver.getCurrentUrl();
            Set winHandels = driver.getWindowHandles();
            List it = new ArrayList(winHandels);
            driver.switchTo().window((String) it.get(0));
            Thread.sleep(1000);
            if(driver.getCurrentUrl().equals(url1)){
                driver.switchTo().window((String) it.get(1));
            }
            System.out.println("URL2：" + driver.getCurrentUrl());
            ifonce=false;
        }
        while (notInterrupted) {


//            for(int i=0;i<5;i++){
//                int j=(int)(Math.random()*3);
//                Thread.sleep(2000);
//                switch (j){
//                    case 0:
//                        two.execut();
//                        break;
//                    case 1:
//                        three.execut();
//                        break;
//                    case 2:
//                        fore.execut();
//                        break;
//                }
//            }
            Thread.sleep(2000);
            if(SYMBLE) {
                one.execut();
                SYMBLE=false;
            }else {
                two.execut();
                SYMBLE=true;
            }
            System.out.println("***************************正在抓取数据:第"+(count++)+"次*******************************");
            Thread.sleep(1000);
            String data = driver.getPageSource();
            Thread.sleep(1000);
            task.initial(data);
            threadPool.submit(task);
            //除去上面已睡眠的3s
            Thread.sleep(DataProcessor.getTimeInterval() * 1000 -2000);
        }
    }

}
