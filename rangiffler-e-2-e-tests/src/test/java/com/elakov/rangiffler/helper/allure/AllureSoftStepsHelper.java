package com.elakov.rangiffler.helper.allure;

import com.google.common.base.Throwables;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AllureSoftStepsHelper {

    private static final Logger log = LoggerFactory.getLogger(AllureSoftStepsHelper.class);

    private List<AbstractMap.SimpleEntry<String, Allure.ThrowableRunnableVoid>> steps = new ArrayList<>();

    private static ThreadLocal<Set<Throwable>> loggedError = new ThreadLocal<>();

    public AllureSoftStepsHelper add(String stepName, Allure.ThrowableRunnableVoid stepRun) {
        steps.add(new AbstractMap.SimpleEntry<>(stepName, stepRun));
        return this;
    }

    public void execute() {
        var fails = new AtomicReference<>(0);
        var lastError = new AtomicReference<Throwable>();

        var isRootSoftStep = loggedError.get() == null;
        if (isRootSoftStep) {
            loggedError.set(new HashSet<>());
        }

        try {
            steps.forEach(stepData -> {
                try {
                    AllureStepHelper.step(stepData.getKey(), () -> {
                        try {
                            stepData.getValue().run();
                        } catch (Throwable throwable) {
                            if (!loggedError.get().contains(throwable) && !(throwable instanceof SoftStepError)) {
                                AllureAttachHelper.addStepParameter("ERROR: ", throwable.getMessage());
                            }
                            throw throwable;
                        }
                    });
                } catch (Throwable e) {
                    if (e instanceof SoftStepError) {
                        fails.set(fails.get() + ((SoftStepError)e).failedStepsCount);
                    } else {
                        fails.set(fails.get() + 1);
                        lastError.set(e);
                        if (!loggedError.get().contains(e)) {
                            loggedError.get().add(e);
                            log.error("", e);
                        }
                    }
                }
            });
        } finally {
            if (isRootSoftStep) {
                loggedError.set(null);
            }
        }

        if (fails.get() > 1) {
            throw new SoftStepError(fails.get());
        } else {
            var e = lastError.get();
            if (e != null) {
                Throwables.throwIfUnchecked(e);
                throw new RuntimeException(e);
            }
        }
    }

    private static class SoftStepError extends AssertionError {
        public int failedStepsCount;

        public SoftStepError(int failedStepsCount) {
            super("ERROR WAS HAPPEND IN {" + failedStepsCount + "}  STEPS -> " +
                    "MORE DETAILS IN ALLURE REPORT AND LOG FILE");
            this.failedStepsCount = failedStepsCount;
        }
    }
}
