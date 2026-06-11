package com.elakov.rangiffler.helper.comparator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Smoke test for the JsonComparator entry point (the json-unit assertion + throw-on-mismatch glue).
 * The rendered HTML itself is pinned by {@link ComparatorRenderingCharacterizationTest}.
 */
class JsonComparatorTest {

    @Test
    void equalJsonPasses() {
        assertThatCode(() -> new JsonComparator()
                .assertThatJson("{\"a\":1,\"b\":[1,2]}")
                .equalsToJson("{\"b\":[1,2],\"a\":1}"))
                .doesNotThrowAnyException();
    }

    @Test
    void mismatchThrows() {
        assertThatThrownBy(() -> new JsonComparator()
                .assertThatJson("{\"a\":1}")
                .equalsToJson("{\"a\":2}"))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    void ignoredPathIsNotCompared() {
        assertThatCode(() -> new JsonComparator()
                .assertThatJson("{\"a\":1,\"id\":\"x\"}")
                .ignorePaths("id")
                .equalsToJson("{\"a\":1,\"id\":\"y\"}"))
                .doesNotThrowAnyException();
    }
}
