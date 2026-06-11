package com.elakov.rangiffler.jupiter;

import com.elakov.rangiffler.jupiter.annotation.RetryingTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Self-test for {@link RetryingTest} / RetryingTestExtension — no external
 * stack needed. The method throws on the first two attempts and passes on the
 * third; the build stays green (the two failures abort, not fail), proving the
 * extension retries on the configured exception and stops on first success.
 */
class RetryingTestExtensionTest {

    private static final AtomicInteger ATTEMPTS = new AtomicInteger(0);

    @RetryingTest(value = 3, onExceptions = IllegalStateException.class)
    void retriesUntilItPasses() {
        int attempt = ATTEMPTS.incrementAndGet();
        if (attempt < 3) {
            throw new IllegalStateException("transient failure on attempt " + attempt);
        }
        assertThat(attempt).isEqualTo(3);
    }
}
