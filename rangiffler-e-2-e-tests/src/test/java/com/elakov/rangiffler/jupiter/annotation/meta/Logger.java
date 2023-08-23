package com.elakov.rangiffler.jupiter.annotation.meta;

import com.elakov.rangiffler.jupiter.callback.utils.AllureLogAttachCallback;
import com.elakov.rangiffler.jupiter.callback.utils.ErrorLoggerCallback;
import com.elakov.rangiffler.jupiter.callback.utils.EventLoggerCallback;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({ErrorLoggerCallback.class, EventLoggerCallback.class, AllureLogAttachCallback.class})
public @interface Logger {
}
