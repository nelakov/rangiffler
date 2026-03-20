package com.elakov.rangiffler.helper.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.util.ResultsUtils;
import lombok.extern.slf4j.Slf4j;

import static io.qameta.allure.Allure.addAttachment;

@Slf4j
public class AllureAttachHelper {

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

}
