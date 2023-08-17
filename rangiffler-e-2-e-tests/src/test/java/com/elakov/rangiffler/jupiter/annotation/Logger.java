package com.elakov.rangiffler.jupiter.annotation;

import com.elakov.rangiffler.jupiter.extension.AllureLogAttachExtension;
import com.elakov.rangiffler.jupiter.extension.ErrorLoggerExtension;
import com.elakov.rangiffler.jupiter.extension.EventLoggerExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({ErrorLoggerExtension.class, EventLoggerExtension.class, AllureLogAttachExtension.class})
public @interface Logger {
}
