package com.elakov.rangiffler.jupiter.annotation.meta;

import com.elakov.rangiffler.jupiter.callback.utils.AllureLogAttachCallback;
import com.elakov.rangiffler.jupiter.callback.utils.ErrorLoggerCallback;
import com.elakov.rangiffler.jupiter.callback.utils.EventLoggerCallback;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Order matters: JUnit runs afterEach callbacks in reverse registration order,
// so AllureLogAttachCallback is registered FIRST to attach LAST — capturing the
// FINISH line (EventLogger) and the failure stack (ErrorLogger) in the same
// per-test log. beforeEach runs in order, so its clear() still happens first.
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({AllureLogAttachCallback.class, EventLoggerCallback.class, ErrorLoggerCallback.class})
public @interface Logger {
}
