package com.elakov.rangiffler.helper;

import com.google.common.base.Throwables;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Soft assertions expressed as Allure steps: collect several checks, run them all, then aggregate
 * the failures into one outcome instead of stopping at the first. Each check is its own Allure step
 * (logged via the registered step listener); a single failure rethrows the original cause, more than
 * one throws a single error summarizing the count.
 *
 * <pre>{@code
 * new AllureSoftSteps()
 *     .add("status is 200", () -> assertThat(resp.code()).isEqualTo(200))
 *     .add("body has id",   () -> assertThat(resp.id()).isNotNull())
 *     .execute();
 * }</pre>
 */
public class AllureSoftSteps {

    private static final Logger log = LoggerFactory.getLogger(AllureSoftSteps.class);

    private final List<AbstractMap.SimpleEntry<String, Allure.ThrowableRunnableVoid>> steps = new ArrayList<>();

    // Shared across nested executes on the same thread so an error that bubbles
    // through several soft-step levels is reported and logged only once.
    private static final ThreadLocal<Set<Throwable>> seenErrors = new ThreadLocal<>();

    public AllureSoftSteps add(String stepName, Allure.ThrowableRunnableVoid stepRun) {
        steps.add(new AbstractMap.SimpleEntry<>(stepName, stepRun));
        return this;
    }

    public void execute() {
        boolean rootStep = enterScope();
        try {
            runSteps().propagate();
        } finally {
            if (rootStep) {
                seenErrors.remove();
            }
        }
    }

    private Failures runSteps() {
        Failures failures = new Failures();
        for (var step : steps) {
            runStep(step.getKey(), step.getValue(), failures);
        }
        return failures;
    }

    private void runStep(String name, Allure.ThrowableRunnableVoid body, Failures failures) {
        try {
            Allure.step(name, () -> runReportingErrorOnce(body));
        } catch (Throwable thrown) {
            failures.record(thrown);
        }
    }

    private void runReportingErrorOnce(Allure.ThrowableRunnableVoid body) throws Throwable {
        try {
            body.run();
        } catch (Throwable thrown) {
            if (!alreadySeen(thrown) && !(thrown instanceof SoftStepError)) {
                Allure.parameter("Error:", thrown.getMessage());
            }
            throw thrown;
        }
    }

    private boolean enterScope() {
        boolean root = seenErrors.get() == null;
        if (root) {
            seenErrors.set(new HashSet<>());
        }
        return root;
    }

    private static boolean alreadySeen(Throwable thrown) {
        return seenErrors.get().contains(thrown);
    }

    private static void logOnce(Throwable thrown) {
        if (!alreadySeen(thrown)) {
            seenErrors.get().add(thrown);
            log.error("Soft step failed", thrown);
        }
    }

    private static class Failures {
        private int count;
        private Throwable lastError;

        void record(Throwable thrown) {
            if (thrown instanceof SoftStepError) {
                count += ((SoftStepError) thrown).failedStepsCount();
            } else {
                count++;
                lastError = thrown;
                logOnce(thrown);
            }
        }

        void propagate() {
            // One failure: rethrow the original so the report shows the real cause.
            // More than one: throw a single aggregate error summarizing the count.
            if (count > 1) {
                throw new SoftStepError(count);
            }
            if (lastError != null) {
                Throwables.throwIfUnchecked(lastError);
                throw new RuntimeException(lastError);
            }
        }
    }

    private static class SoftStepError extends AssertionError {
        private final int failedStepsCount;

        SoftStepError(int failedStepsCount) {
            super(failedStepsCount + " step(s) failed. " +
                    "See the Allure report steps and the log files for details.");
            this.failedStepsCount = failedStepsCount;
        }

        int failedStepsCount() {
            return failedStepsCount;
        }
    }
}
