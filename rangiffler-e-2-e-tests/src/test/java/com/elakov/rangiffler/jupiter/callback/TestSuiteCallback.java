package com.elakov.rangiffler.jupiter.callback;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

public interface TestSuiteCallback extends BeforeAllCallback {

    default void beforeSuite() {
    }

    default void afterSuite() {
    }

    @Override
    default void beforeAll(ExtensionContext context) throws Exception {
        context.getRoot().getStore(Namespace.GLOBAL)
                .getOrComputeIfAbsent(
                        TestSuiteCallback.class,
                        k -> {
                            beforeSuite();
                            return (ExtensionContext.Store.CloseableResource) this::afterSuite;
                        });
    }

}
