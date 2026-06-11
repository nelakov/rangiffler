package com.elakov.rangiffler.jupiter.annotation;

import com.elakov.rangiffler.jupiter.callback.RetryingTestExtension;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Re-runs a test until it passes or the attempt budget is exhausted — for
 * e2e tests that flake on transient infrastructure (parallel data-setup
 * races, gRPC UNAVAILABLE, slow first hit). Replaces @Test.
 *
 * Retries only on {@link #onExceptions()} (default: any Throwable). An
 * AssertionError is a retriable Throwable by default; narrow onExceptions to
 * e.g. {NullPointerException.class, io.grpc.StatusRuntimeException.class} when
 * you only want to absorb infra noise and still fail fast on real assertion
 * mismatches.
 *
 * Pinned to SAME_THREAD so the per-method retry state in the extension store
 * is not raced by the parallel executor.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(RetryingTestExtension.class)
@Execution(ExecutionMode.SAME_THREAD)
public @interface RetryingTest {

    int value() default 3;

    Class<? extends Throwable>[] onExceptions() default {Throwable.class};
}
