package com.elakov.rangiffler.jupiter.extension;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class ErrorLoggerExtension implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext info) throws Exception {
        Throwable e = info.getExecutionException().orElse(null);
        if (e != null) {
            log.error("", e);
        }
    }

}
