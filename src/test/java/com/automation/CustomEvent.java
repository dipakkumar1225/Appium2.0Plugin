package com.automation;

import io.appium.java_client.proxy.MethodCallListener;
import io.appium.java_client.proxy.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;

public class CustomEvent implements MethodCallListener {

    public Object onError(Object obj, Method method, Object[] args, Throwable e) throws Throwable {
        throw new NotImplementedException();

    }
}