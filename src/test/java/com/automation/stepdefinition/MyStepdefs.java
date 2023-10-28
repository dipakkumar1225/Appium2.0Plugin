package com.automation.stepdefinition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

public class MyStepdefs {
    @Given("Case scenario {int} : {int}")
    public void caseScenario(int arg0, int arg1) {
        
    }
    @Given("Case scenario datatable")
    public void caseScenarioDataTable(DataTable dataTable) {
        List<Map<String,String>> maps = dataTable.asMaps();
        System.out.println("MAP " + maps);

    }

    @When("scenario condition {int} : {int}")
    public void scenarioCondition(int arg0, int arg1) {
        
    }

    @Then("scenario assertion {int} : {int}")
    public void scenarioAssertion(int arg0, int arg1) {
    }
}
