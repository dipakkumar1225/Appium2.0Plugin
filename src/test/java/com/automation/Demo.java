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
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;

public class Demo {

    private final String LOG_DIRECTORY = System.getProperty("user.dir") + File.separator + "ProxyLog";
    private final String LOG_FILE_PATH = LOG_DIRECTORY + File.separator + "mitmproxy.log";

    private void ensureLogDirectoryExists() {
        File dir = new File(LOG_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private void ensureLogFileExists() throws IOException {
        File logFile = new File(LOG_FILE_PATH);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
    }
    @AndroidFindBy(accessibility = "open menu")
    private WebElement webElementOpenMenu;

    @AndroidFindBy(accessibility = "menu item log in")
    private WebElement webElementNavSubMenuLogIn;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(0)")
    private WebElement webElementInputUserName;
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.EditText\").instance(1)")
    private WebElement webElementInputPassword;
    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.view.ViewGroup\").description(\"Login button\")")
    private WebElement webElementBtnLogin;

    @AndroidFindBy(accessibility = "menu item log out")
    private WebElement webElementNavSubMenuLogout;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/alertTitle\")")
    private WebElement webElementAlertTitle;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/message\")")
    private WebElement webElementAlertMessage;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/button1\")")
    private WebElement webElementAlertButton1;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/button2\")")
    private WebElement webElementAlertButton2;

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
//                .withArgument(() -> "--config", new File("config" + File.separator + "serverconfig.json").toString())
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

//        System.out.println("getWaitPluginProperties OUTPUT ::-  " +   appiumOriginalDriver.executeScript("plugin: getWaitPluginProperties", ImmutableMap.of()));

        webElementOpenMenu.click();

        webElementNavSubMenuLogIn.click();

        webElementInputUserName.sendKeys("bob@example.com");
        webElementInputPassword.sendKeys("10203040");
        webElementBtnLogin.click();

        webElementOpenMenu.click();
        webElementNavSubMenuLogout.click();

        clickAlertButtonByLabel(ButtonLabel.LOGOUT);
        clickAlertButtonByLabel(ButtonLabel.OK);

      /*  ensureLogDirectoryExists();
        try {
            ensureLogFileExists();
            Files.write(Paths.get(LOG_FILE_PATH), ("Hello World" + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
            e.printStackTrace();
        }*/

        appiumOriginalDriver.quit();
        appiumDriverLocalService.stop();

    }

    public void clickAlertButtonByLabel(ButtonLabel buttonLabel) {
        switch (buttonLabel) {
            case OK, LOGOUT -> webElementAlertButton1.click();
            case CANCEL -> webElementAlertButton2.click();
            default -> throw new IllegalArgumentException("Unknown button label: " + buttonLabel);
        }
    }

    public enum ButtonLabel {
        LOGOUT("LOG OUT"),
        CANCEL("CANCEL"),
        OK("OK");
        private final String label;
        ButtonLabel(String label) {
            this.label = label;
        }

    }
}

