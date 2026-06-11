package com.elakov.rangiffler.jupiter;

import com.elakov.rangiffler.jupiter.annotation.RetryingTest;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Self-test for {@link RetryingTest} — proves the test-BODY retry path with no
 * external stack: the method fails its first two attempts and passes on the
 * third, and the build stays green because the failed attempts abort rather
 * than fail. The @BeforeEach (setup) retry path is covered by
 * {@link RetryingTestSetupFlakeTest}.
 */
class RetryingTestExtensionTest {

    private static final AtomicInteger ATTEMPTS = new AtomicInteger(0);

    @RetryingTest(value = 3, onExceptions = IllegalStateException.class)
    void retriesBodyUntilItPasses() {
        int attempt = ATTEMPTS.incrementAndGet();
        if (attempt < 3) {
            throw new IllegalStateException("transient body failure on attempt " + attempt);
        }
        assertThat(attempt).isEqualTo(3);
    }
}
