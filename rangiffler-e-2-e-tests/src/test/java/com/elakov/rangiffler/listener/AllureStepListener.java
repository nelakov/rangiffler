package com.elakov.rangiffler.listener;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllureStepListener implements StepLifecycleListener {

    public static final String STEP_PREFIX = "STEP --> ";

    @Override
    public void beforeStepStart(StepResult result) {
        if (!"step".equals(result.getName())) {
            // Prefix so AllureLogAppender colors step lines distinctly in the per-test log.
            log.info(STEP_PREFIX + result.getName());
        }
        StepLifecycleListener.super.beforeStepStart(result);
    }

    @Override
    public void beforeStepUpdate(StepResult result) {
        StepLifecycleListener.super.beforeStepUpdate(result);
    }
}
