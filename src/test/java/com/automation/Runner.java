package com.automation;

import io.cucumber.java.*;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        tags = "@Sanity",
        glue = {
                "com.automation.stepdefinition"
        },
        plugin = {
                "pretty"
                , "summary"
                , "com.automation.CucumberListener"
                , "html:target/cucumber-reports/Moto_Edge_30/Moto_Edge_30.html"
                , "json:target/cucumber-reports/Moto_Edge_30/Moto_Edge_30.json"
        },
        features = {
                "classpath:features/Demo1.feature",
                "classpath:features/Demo2.feature",
                "classpath:features/Demo3.feature",
        }
)
public class Runner extends AbstractTestNGCucumberTests {

//    static Demo_22 demo22;

//    @BeforeAll()
//    public static void initAppiumServer() {
//        demo22 = new Demo_22();
//        demo22.initAppiumServer();
//    }
//
//    @Before()
//    public void initDriver() {
//        demo22.initDriver();
//    }
//
//    @BeforeStep
//    public void beforeStep() {
//            demo22.actionOnElement();
//    }
//
//    @After()
//    public void quitDriver() {
//        demo22.quitDriver();
//    }
//
//
//    @AfterAll()
//    public static void quitAppiumServer() {
//        demo22.tearDownServer();
//    }
}
