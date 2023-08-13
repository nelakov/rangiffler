package com.elakov.rangiffler.jupiter.annotation;

import com.elakov.rangiffler.jupiter.extension.EnvironmentExecutionCondition;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ExtendWith(EnvironmentExecutionCondition.class)
@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface Env {

    String[] enabledFor() default {"local", "docker"};
}
