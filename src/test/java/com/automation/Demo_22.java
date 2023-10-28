package com.automation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

public class Demo_22 {

    @AndroidFindBy(accessibility = "open menu")
    private WebElement webElementOpenMenu;
    AppiumDriverLocalService appiumDriverLocalService;
    public void initAppiumServer(){
        appiumDriverLocalService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.USE_DRIVERS, "uiautomator2")
                .withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/")
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info:debug")
                .withTimeout(Duration.ofSeconds(180)));
        appiumDriverLocalService.start();
    }

    AppiumDriver appiumDriver;
    public void initDriver(){
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options()
                .setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2)
                .setApp(System.getProperty("user.dir" + File.separator + "app" + File.separator + "android" + File.separator + "Android-MyDemoAppRN.1.3.0.build-244.apk"))
                .setFullReset(false)
                .setNoReset(true)
                .setPlatformName("android")
                .setPlatformVersion("13")
                .setUdid("ZD222CJSXB")
                .setAppPackage("com.saucelabs.mydemoapp.rn")
                .setAppActivity("com.saucelabs.mydemoapp.rn.MainActivity");

        appiumDriver = new AndroidDriver(appiumDriverLocalService.getUrl(), uiAutomator2Options);
    }

    public void quitDriver(){
        appiumDriver.quit();
    }

    public void tearDownServer(){
        appiumDriverLocalService.stop();
    }

    public void actionOnElement() {
        PageFactory.initElements(new AppiumFieldDecorator(appiumDriver, Duration.ofSeconds(30)), this);
        webElementOpenMenu.click();

    }
}


//WebElement webElementOpenMenu_1 = eventDriver.findElement(AppiumBy.accessibilityId("open menu")); --- This is working

   /*     EventFiringWebDriver eventDriver = new EventFiringWebDriver(appiumDriver);
        CustomEventListener listener = new CustomEventListener();
        eventDriver.register(listener);
*/