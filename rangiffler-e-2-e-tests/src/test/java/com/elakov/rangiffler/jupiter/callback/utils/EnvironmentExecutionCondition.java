package com.elakov.rangiffler.jupiter.callback.utils;

import com.elakov.rangiffler.jupiter.annotation.meta.Env;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;

@Slf4j
public class EnvironmentExecutionCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        String activeEnvironment = System.getProperty("env");
        if (activeEnvironment == null) {
            activeEnvironment = System.getenv("env");
        } else if (activeEnvironment == null) {
            return disabled("there's no active environment");
        }

        Set<String> enabledEnvironments = getEnabledEnvironments(context);
        return enabledEnvironments.contains(activeEnvironment)
                ? enabled("active environment is enabled")
                : disabled("active environment is not enabled");
    }

    private Set<String> getEnabledEnvironments(ExtensionContext context) {
        Set<String> enabledEnvironments = new HashSet<>();
        // method-level @Env wins; fall back to the class-level annotation, otherwise
        // every method of an @Env-annotated class is silently disabled
        context.getElement().flatMap(element -> AnnotationSupport.findAnnotation(element, Env.class))
                .or(() -> context.getTestClass().flatMap(clazz -> AnnotationSupport.findAnnotation(clazz, Env.class)))
                .map(Env::enabledFor)
                .ifPresent(array -> enabledEnvironments.addAll(Arrays.asList(array)));
        return enabledEnvironments;
    }
}
