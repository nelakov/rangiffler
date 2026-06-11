package com.elakov.rangiffler.jupiter.callback;

import com.elakov.rangiffler.jupiter.annotation.RetryingTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.platform.commons.support.AnnotationSupport;
import org.opentest4j.TestAbortedException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Drives {@link RetryingTest}. Provides one test-template invocation at a time;
 * a failed invocation is converted to an aborted one (so it doesn't fail the
 * build) as long as attempts remain and the exception is retriable. The first
 * success ends the stream; exhausting the budget rethrows the last failure.
 */
public class RetryingTestExtension
        implements TestTemplateInvocationContextProvider, TestExecutionExceptionHandler {

    private static final Namespace NAMESPACE = Namespace.create(RetryingTestExtension.class);

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getTestMethod()
                .map(m -> AnnotationSupport.isAnnotated(m, RetryingTest.class))
                .orElse(false);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        RetryingTest config = context.getRequiredTestMethod().getAnnotation(RetryingTest.class);
        RetryState state = new RetryState(config.value(), config.onExceptions());
        context.getStore(NAMESPACE).put(context.getRequiredTestMethod(), state);

        Iterator<TestTemplateInvocationContext> iterator = new Iterator<>() {
            @Override
            public boolean hasNext() {
                return state.shouldRunAnother();
            }

            @Override
            public TestTemplateInvocationContext next() {
                state.startAttempt();
                return new RetryInvocationContext(state.currentAttempt(), config.value());
            }
        };
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        RetryState state = context.getStore(NAMESPACE)
                .get(context.getRequiredTestMethod(), RetryState.class);

        boolean retriable = Arrays.stream(state.onExceptions())
                .anyMatch(type -> type.isInstance(throwable));

        if (retriable && state.attemptsLeft()) {
            // absorb this failure; the iterator will produce the next attempt
            state.requestRetry();
            throw new TestAbortedException(
                    "Attempt " + state.currentAttempt() + " failed, retrying: " + throwable);
        }
        // not retriable, or budget exhausted -> real failure
        throw throwable;
    }

    private static final class RetryState {
        private final int maxAttempts;
        private final Class<? extends Throwable>[] onExceptions;
        private int attemptsStarted = 0;
        private boolean retryRequested = false;

        RetryState(int maxAttempts, Class<? extends Throwable>[] onExceptions) {
            this.maxAttempts = maxAttempts;
            this.onExceptions = onExceptions;
        }

        Class<? extends Throwable>[] onExceptions() {
            return onExceptions;
        }

        int currentAttempt() {
            return attemptsStarted;
        }

        boolean attemptsLeft() {
            return attemptsStarted < maxAttempts;
        }

        /**
         * Run the first attempt unconditionally; afterwards run another only if the
         * previous attempt requested a retry (i.e. failed retriably with budget left).
         * A successful attempt never sets retryRequested, so the stream ends.
         */
        boolean shouldRunAnother() {
            return attemptsStarted == 0 || retryRequested;
        }

        void startAttempt() {
            attemptsStarted++;
            retryRequested = false;
        }

        void requestRetry() {
            retryRequested = true;
        }
    }

    /** Marks an invocation succeeded once it finishes without the exception handler firing. */
    private record RetryInvocationContext(int attempt, int max) implements TestTemplateInvocationContext {
        @Override
        public String getDisplayName(int invocationIndex) {
            return "attempt " + attempt + "/" + max;
        }
    }
}
