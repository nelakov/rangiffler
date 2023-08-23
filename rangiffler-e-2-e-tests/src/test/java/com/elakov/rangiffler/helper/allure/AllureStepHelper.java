package com.elakov.rangiffler.helper.allure;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;

import static com.elakov.rangiffler.listener.AllureStepListener.STEP_PREFIX;

@Slf4j
public class AllureStepHelper {

    public static void step(String name, Allure.ThrowableRunnableVoid runnable) {
        log.info(STEP_PREFIX + name);
        Allure.step(name, runnable);
    }

    public static <T> T step(String name, Allure.ThrowableRunnable<T> runnable) {
        log.info(STEP_PREFIX + name);
        return Allure.step(name, runnable);
    }

}