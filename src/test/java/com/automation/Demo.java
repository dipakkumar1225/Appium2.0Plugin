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
import java.net.MalformedURLException;
import java.time.Duration;

public class Demo {

    @AndroidFindBy(accessibility = "open menu")
    private WebElement webElementOpenMenu;

    @Test()
    public void SampleWaitPlugin() throws MalformedURLException {

        AppiumDriverLocalService appiumDriverLocalService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.USE_DRIVERS, "uiautomator2")
                .withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/")
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info:debug")
                .withArgument(GeneralServerFlag.USE_PLUGINS, "element-wait")
                .withArgument(GeneralServerFlag.CONFIGURATION_FILE, new File("config" + File.separator + "serverconfig.json").toString())
                .withTimeout(Duration.ofSeconds(180)));
        appiumDriverLocalService.start();

        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options()
                .setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2)
                .setApp(System.getProperty("user.dir") + File.separator + "app" + File.separator + "android" + File.separator + "Android-MyDemoAppRN.1.3.0.build-244.apk")
                .setFullReset(false)
                .setNoReset(true)
                .setPlatformName("android")
                .setPlatformVersion("13")
                .setUdid("ZD222CJSXB")
                .setAppPackage("com.saucelabs.mydemoapp.rn")
                .setAppActivity("com.saucelabs.mydemoapp.rn.MainActivity");

        AppiumDriver appiumOriginalDriver = new AndroidDriver(appiumDriverLocalService.getUrl(), uiAutomator2Options);
        PageFactory.initElements(new AppiumFieldDecorator(appiumOriginalDriver, Duration.ofSeconds(30)), this);
        webElementOpenMenu.click();
        appiumDriverLocalService.stop();
    }
}

/*
//        WebDriverListener webDriverListener = new CustomEventListener();
//        AppiumDriver decorated = new EventFiringDecorator<>(AppiumDriver.class, webDriverListener).decorate(appiumOriginalDriver);

List<MethodCallListener> listeners = Collections.singletonList(new CustomEvent());
//
AppiumDriver decoratedDriver = Helpers.createProxy(
AndroidDriver.class,
new Object[] {new URL("http://127.0.0.1:4723/"), new UiAutomator2Options()},
new Class[] {URL.class, UiAutomator2Options.class},
listeners
);*/
