package com.elakov.rangiffler.jupiter.callback.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

@Slf4j
public class EventLoggerCallback implements BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        log.info("################################### START TEST EXECUTION {} ###################################", getTestName(context));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        log.info("################################### FINISHED TEST EXECUTION {} ###################################", getTestName(context));
    }

    public static String getTestName(ExtensionContext context) {
        String className = context.getTestClass().map(Class::getName).orElse("");
        String methodName = context.getTestMethod().map(Method::getName).orElse("");
        return (className + "_" + methodName)
                .replace(".", "_")
                .replace("$", "_");
    }
}