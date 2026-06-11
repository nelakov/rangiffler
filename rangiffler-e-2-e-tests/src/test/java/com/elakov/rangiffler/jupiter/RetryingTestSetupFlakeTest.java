package com.elakov.rangiffler.jupiter;

import com.elakov.rangiffler.jupiter.annotation.RetryingTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Proves {@link RetryingTest} retries failures thrown from @BeforeEach setup —
 * the case that matters for e2e, where data-setup callbacks (@CreateUser /
 * @ApiLogin) flake. The setup throws on the first two attempts; the test body
 * is reached only once setup stops failing, so reaching it at all proves the
 * @BeforeEach failures were retried (via LifecycleMethodExecutionExceptionHandler)
 * rather than propagated.
 */
class RetryingTestSetupFlakeTest {

    private static final AtomicInteger SETUP_ATTEMPTS = new AtomicInteger(0);

    private boolean setupSucceeded;

    @BeforeEach
    void flakySetup() {
        if (SETUP_ATTEMPTS.incrementAndGet() < 3) {
            throw new IllegalStateException("transient setup failure #" + SETUP_ATTEMPTS.get());
        }
        setupSucceeded = true;
    }

    @RetryingTest(value = 3, onExceptions = IllegalStateException.class)
    void reachedOnlyAfterSetupRetriesSucceed() {
        assertThat(setupSucceeded).isTrue();
        assertThat(SETUP_ATTEMPTS.get()).isEqualTo(3);
    }
}
