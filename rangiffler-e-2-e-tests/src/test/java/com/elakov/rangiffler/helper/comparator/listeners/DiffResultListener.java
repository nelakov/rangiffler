package com.elakov.rangiffler.helper.comparator.listeners;

import net.javacrumbs.jsonunit.core.listener.Difference;
import net.javacrumbs.jsonunit.core.listener.DifferenceContext;
import net.javacrumbs.jsonunit.core.listener.DifferenceListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Collects the differences json-unit reports during a comparison, then lets the diff renderer
 * look one up by its actual- or expected-side path.
 */
public class DiffResultListener implements DifferenceListener {

    private final List<Difference> diffs = new ArrayList<>();

    @Override
    public void diff(Difference difference, DifferenceContext context) {
        diffs.add(difference);
    }

    public Difference actualDiff(String path) {
        return find(path, Difference::getActualPath);
    }

    public Difference expectedDiff(String path) {
        return find(path, Difference::getExpectedPath);
    }

    private Difference find(String path, Function<Difference, String> pathOf) {
        for (Difference diff : diffs) {
            if (Objects.equals(path, pathOf.apply(diff))) {
                return diff;
            }
        }
        return null;
    }
}
