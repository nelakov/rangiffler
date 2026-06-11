package com.elakov.rangiffler.helper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AllureSoftStepsTest {

    @Test
    void allStepsPass() {
        assertThatCode(() -> new AllureSoftSteps()
                .add("one", () -> assertThat(1).isEqualTo(1))
                .add("two", () -> assertThat("x").isEqualTo("x"))
                .execute())
                .doesNotThrowAnyException();
    }

    @Test
    void singleFailureRethrowsOriginalCause() {
        assertThatThrownBy(() -> new AllureSoftSteps()
                .add("ok", () -> assertThat(1).isEqualTo(1))
                .add("boom", () -> {
                    throw new IllegalStateException("explode");
                })
                .execute())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("explode");
    }

    @Test
    void multipleFailuresAggregateWithCount() {
        assertThatThrownBy(() -> new AllureSoftSteps()
                .add("fail-1", () -> assertThat(1).isEqualTo(2))
                .add("ok", () -> assertThat(1).isEqualTo(1))
                .add("fail-2", () -> assertThat("a").isEqualTo("b"))
                .execute())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("2 step(s) failed");
    }

    @Test
    void allStepsRunEvenWhenEarlyOneFails() {
        boolean[] lastRan = {false};
        assertThatThrownBy(() -> new AllureSoftSteps()
                .add("fail-early", () -> assertThat(1).isEqualTo(2))
                .add("still-runs", () -> lastRan[0] = true)
                .execute())
                .isInstanceOf(Throwable.class);
        assertThat(lastRan[0]).as("steps after a failure still execute").isTrue();
    }
}
