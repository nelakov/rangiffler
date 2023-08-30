package com.elakov.rangiffler.jupiter.callback.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class ErrorLoggerCallback implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext info) throws Exception {
        Throwable e = info.getExecutionException().orElse(null);
        if (e != null) {
            log.error("", e);
        }
    }

}
