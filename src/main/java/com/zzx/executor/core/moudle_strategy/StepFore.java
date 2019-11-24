package com.zzx.executor.core.moudle_strategy;

import com.zzx.executor.core.IEOperator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StepFore extends AbstractStrategy implements IStrategy {
    @Autowired
    IEOperator ieOperator;

    @Override
    public void execut() {
        System.out.println("步骤四！");
        WebElement el= ieOperator.driver.findElements(By.className("tabs-mod__tab___1dUYL")).get(4);
        common(el);
    }
    @Override
    public void common(WebElement el){
        JavascriptExecutor js = (JavascriptExecutor) ieOperator.driver;
        js.executeScript("arguments[0].click();",el);
    }
}
