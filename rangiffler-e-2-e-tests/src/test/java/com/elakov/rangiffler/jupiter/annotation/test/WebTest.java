package com.elakov.rangiffler.jupiter.annotation.test;

import com.elakov.rangiffler.jupiter.callback.BrowserConfigExtension;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({AllureJunit5.class, BrowserConfigExtension.class})
public @interface WebTest {

}