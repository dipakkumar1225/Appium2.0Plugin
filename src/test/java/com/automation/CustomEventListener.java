package com.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class CustomEventListener implements WebDriverListener {
    public void beforeFindElement(WebDriver driver, By locator) {
    }

    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
    }

 /*   public Object onError(Object obj, Method method, Object[] args, Throwable e) throws Throwable {
        throw new NotImplementedException();
    }
*/
}