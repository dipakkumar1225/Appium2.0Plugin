package com.automation;

//https://community.smartbear.com/t5/Cucumber-Open/Ways-to-find-the-total-number-of-scenarios-when-the-test/m-p/240359?lightbox-message-images-240359=15464i911027FDF3E8463B

import io.cucumber.core.feature.FeatureParser;
import io.cucumber.core.filter.Filters;
import io.cucumber.core.gherkin.Pickle;
import io.cucumber.core.options.CommandlineOptionsParser;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.core.resource.ClassLoaders;
import io.cucumber.core.runtime.FeaturePathFeatureSupplier;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import io.cucumber.tagexpressions.Expression;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CucumberListener implements ConcurrentEventListener {
    public static String stepName;
    private final ThreadLocal<AtomicInteger> executedScenariosCount = ThreadLocal.withInitial(AtomicInteger::new);
    private final ThreadLocal<String> previousFeatureFile = ThreadLocal.withInitial(() -> "");
    private final ThreadLocal<String> currentFeatureFile = ThreadLocal.withInitial(() -> "");
    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {

        eventPublisher.registerHandlerFor(TestRunStarted.class, this::beforeTestRun);
        eventPublisher.registerHandlerFor(TestSourceRead.class, this::readFeatureFile);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::afterTestRun);

        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::testCaseStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::testCaseFinished);

        eventPublisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    }

    private void beforeTestRun(TestRunStarted testRunStarted) {
        System.out.println("TEST RUN STARTED : ");
    }
    private void readFeatureFile(TestSourceRead testSourceRead) {
        String featureLocation = testSourceRead.getUri().toString();
        System.out.println("FEATURE FILE PATH : " + featureLocation);
    }

;
    int totalScenarios = 0;
    private void testCaseStarted(TestCaseStarted testCaseStarted) {

        currentFeatureFile.set(testCaseStarted.getTestCase().getUri().toString());

        if (!currentFeatureFile.get().equals(previousFeatureFile.get())) {
            System.out.println("NEW FEATURE FILE STARTED: " + currentFeatureFile.get());
            previousFeatureFile.set(currentFeatureFile.get());
            executedScenariosCount.get().set(1);
        }

        List<String> tags = testCaseStarted.getTestCase().getTags().stream().parallel().collect(Collectors.toList());

        CustomTagExpression customTagExpression = null;
        for (String tag : tags) {
            customTagExpression = new CustomTagExpression(tag);
        }

        System.out.println("customTagExpression " + customTagExpression);
        CommandlineOptionsParser commandlineoptionsParser = new CommandlineOptionsParser(System.out);
        RuntimeOptions runtimeoptions;
        if (customTagExpression == null){
            runtimeoptions = commandlineoptionsParser
                    .parse(currentFeatureFile.get())
                    .addDefaultGlueIfAbsent()
                    .addDefaultFeaturePathIfAbsent()
                    .addDefaultSummaryPrinterIfNotDisabled()
                    .enablePublishPlugin()
                    .build();
        }else {
            runtimeoptions = commandlineoptionsParser
                    .parse(currentFeatureFile.get())
                    .addDefaultGlueIfAbsent()
                    .addDefaultFeaturePathIfAbsent()
                    .addTagFilter(customTagExpression)
                    .addDefaultSummaryPrinterIfNotDisabled()
                    .enablePublishPlugin()
                    .build();
        }
        FeatureParser parser = new FeatureParser(UUID::randomUUID);
        FeaturePathFeatureSupplier featureSupplier = new FeaturePathFeatureSupplier(ClassLoaders::getDefaultClassLoader, runtimeoptions, parser);
        Predicate<Pickle> filter = new Filters(runtimeoptions);
        long countOfPickles = featureSupplier.get().stream()
                .flatMap(feature -> feature.getPickles().stream())
                .filter(filter)
                .count();
        totalScenarios = (int) countOfPickles;

        String featureFileName = currentFeatureFile.get().substring(currentFeatureFile.get().indexOf(':') + 1);

        System.out.println(System.lineSeparator() + "[" + executedScenariosCount.get().getAndIncrement() + "] STARTING SCENARIO : " + testCaseStarted.getTestCase().getName() + " #" + featureFileName + ":" + testCaseStarted.getTestCase().getLocation().getLine());
        System.out.println("TAGS PRESENT : " + tags);
    }

    private void stepStarted(TestStepStarted testStepStarted) {
        if (testStepStarted.getTestStep() instanceof PickleStepTestStep) {
            final PickleStepTestStep ev = (PickleStepTestStep) testStepStarted.getTestStep();
            final String args = StringUtils.join(ev.getDefinitionArgument().stream().map(Argument::getValue).toArray(), ",");
            stepName = ev.getStep().getText();
            if (StringUtils.isNotBlank(args)) {
                stepName += (" : arguments in this steps = (" + args + ")");
            }
            System.out.println("STARTING STEP : " + stepName);
        }
    }

    private void stepFinished(TestStepFinished testStepFinished) {
        if (testStepFinished.getTestStep() instanceof PickleStepTestStep) {
            System.out.println("STEP FINISHED : " + ((PickleStepTestStep) testStepFinished.getTestStep()).getStep().getText());
            switch (testStepFinished.getResult().getStatus()) {
                case PASSED -> onPassedStep(testStepFinished);
                case SKIPPED -> onSkippedStep(testStepFinished);
                case PENDING -> System.out.println("PENDING");
                case UNDEFINED -> System.out.println("UNDEFINED");
                case AMBIGUOUS -> System.out.println("AMBIGUOUS");
                case FAILED -> onFailedStep(testStepFinished);
                case UNUSED -> System.out.println("UNUSED");
            }
        }
    }

    public void onPassedStep(TestStepFinished testStepPassed) {
        Status strStatus = testStepPassed.getResult().getStatus();
        System.out.println("STEP STATUS : " + strStatus);
    }

    public void onSkippedStep(TestStepFinished testStepSkipped) {
        Status strStatus = testStepSkipped.getResult().getStatus();
        System.out.println("STEP STATUS : " + strStatus);
    }

    public void onFailedStep(TestStepFinished testStepFailed) {
        Status strStatus = testStepFailed.getResult().getStatus();
        String strReason = testStepFailed.getResult().getError().getMessage();
        System.out.println("STEP STATUS : " + strStatus + " " + strReason);
    }

    private void testCaseFinished(TestCaseFinished testCaseFinished) {

        String strTestName = testCaseFinished.getTestCase().getName();
        System.out.println("FINISHED SCENARIO : " + strTestName);

        int executedScenarios = (executedScenariosCount.get().get() - 1);
        System.out.println("executedScenarios  " + executedScenarios);

        System.out.println("totalScenarios  " + totalScenarios);

        if (executedScenarios == totalScenarios) {
            System.out.println("FEATURE FILE COMPLETED: " + currentFeatureFile.get());
        }

        switch (testCaseFinished.getResult().getStatus()) {
            case PASSED -> onPassedTest(testCaseFinished);
            case SKIPPED -> onSkippedTest(testCaseFinished);
            case PENDING -> System.out.println("PENDING");
            case UNDEFINED -> System.out.println("UNDEFINED");
            case AMBIGUOUS -> System.out.println("AMBIGUOUS");
            case FAILED -> onFailedTest(testCaseFinished);
            case UNUSED -> System.out.println("UNUSED");
        }
    }

    public void onPassedTest(TestCaseFinished testCasePassed) {
        Status strStatus = testCasePassed.getResult().getStatus();
        System.out.println("SCENARIO STATUS : " + strStatus);
    }

    public void onSkippedTest(TestCaseFinished testCaseSkipped) {
        Status strStatus = testCaseSkipped.getResult().getStatus();
        String strReason = testCaseSkipped.getResult().getError().getMessage();
        System.out.println("SCENARIO STATUS : " + strStatus + " " + strReason);
    }

    public void onFailedTest(TestCaseFinished testCaseFailed) {
        Status strStatus = testCaseFailed.getResult().getStatus();
        String strReason = testCaseFailed.getResult().getError().getMessage();
        System.out.println("SCENARIO STATUS : " + strStatus + " " + strReason);
    }

    private void afterTestRun(TestRunFinished testRunFinished) {
        System.out.println("END Of TEST CASE : " + testRunFinished.getResult());
    }

    static class CustomTagExpression implements Expression {
        private final String desiredTag;

        public CustomTagExpression(String desiredTag) {
            this.desiredTag = desiredTag;
        }

        @Override
        public boolean evaluate(List<String> tags) {
            return tags.contains(desiredTag);
        }
    }
}
