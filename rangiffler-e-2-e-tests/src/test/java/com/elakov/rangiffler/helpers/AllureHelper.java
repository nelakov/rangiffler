package com.elakov.rangiffler.helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.util.ResultsUtils;
import lombok.extern.slf4j.Slf4j;

import static com.elakov.rangiffler.listeners.AllureStepListener.STEP_PREFIX;
import static io.qameta.allure.Allure.addAttachment;

@Slf4j
public class AllureHelper {

    public static void step(String name, Allure.ThrowableRunnableVoid runnable) {
        log.info(STEP_PREFIX + name);
        Allure.step(name, runnable);
    }

    public static <T> T step(String name, Allure.ThrowableRunnable<T> runnable) {
        log.info(STEP_PREFIX + name);
        return Allure.step(name, runnable);
    }

    public static void addStepParameter(String name, Object value) {
        log.info("      " + name + ": " + value);
        Allure.getLifecycle().updateStep(r -> r
                .getParameters().add(ResultsUtils.createParameter(name, value)));
    }

    public static void attachText(String fileName, String content) {
        addAttachment(fileName, "text/plain", content, "txt");
    }

    public static void attachJson(String fileName, String content) {
        addAttachment(fileName, "application/json", content, "json");
    }



    // TODO: Добавить аттачи картинки и видео

}