package com.elakov.rangiffler.helpers;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;

import static com.elakov.rangiffler.listeners.AllureStepListener.STEP_PREFIX;

@Slf4j
public class AllureSteps {

    public static void step(String name, Allure.ThrowableRunnableVoid runnable) {
        log.info(STEP_PREFIX + name);
        Allure.step(name, runnable);
    }

    public static <T> T step(String name, Allure.ThrowableRunnable<T> runnable) {
        log.info(STEP_PREFIX + name);
        return Allure.step(name, runnable);
    }

}