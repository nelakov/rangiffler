package com.elakov.rangiffler.helper.comparator;

import com.elakov.rangiffler.helper.comparator.listeners.DiffResultListener;
import com.elakov.rangiffler.helper.comparator.report.html.DiffReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import io.qameta.allure.Allure;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Fluent JSON equality assertion that attaches a side-by-side Actual/Expect HTML diff to the Allure
 * report — differing paths highlighted — so a failure shows exactly what mismatched. Wraps
 * json-unit for the comparison; the diff tree/report is rendered by this package.
 *
 * <pre>{@code
 * new JsonComparator().assertThatJson(responseBody).ignorePaths("id", "createdAt").equalsToJson(expected);
 * }</pre>
 */
public class JsonComparator {

    private static final String REPORT_NAME = "Diff Report";

    private Object actual;
    private final Set<String> ignoredPaths = new HashSet<>();
    private final List<Line> actualLines = new ArrayList<>();
    private final List<Line> expectedLines = new ArrayList<>();
    private final DiffResultListener listener;
    private final ObjectMapper mapper;

    public JsonComparator() {
        this(new DiffResultListener(), new ObjectMapper());
    }

    private JsonComparator(DiffResultListener listener, ObjectMapper mapper) {
        this.listener = listener;
        this.mapper = mapper;
    }

    public JsonComparator assertThatJson(String json) {
        this.actual = parse(json);
        return this;
    }

    public JsonComparator assertThatObject(Object obj) {
        return assertThatJson(toJson(obj));
    }

    public JsonComparator ignorePaths(String... ignore) {
        ignoredPaths.addAll(Arrays.asList(ignore));
        return this;
    }

    public void equalsToJson(String expect) {
        equalsTo(parse(expect));
    }

    private void equalsTo(Object expect) {
        Throwable failure = runAssertion(expect);
        // The diff report is built and attached on both pass and fail, so the report always
        // shows the compared trees even for a green check.
        buildDiffLines(expect);
        Allure.addAttachment(REPORT_NAME, "text/html",
                new DiffReport(ignoredPaths).asHtml(actualLines, expectedLines), ".html");
        if (failure != null) {
            Throwables.throwIfUnchecked(failure);
            throw new RuntimeException(failure);
        }
    }

    private Throwable runAssertion(Object expect) {
        var asserter = JsonAssertions.assertThatJson(actual)
                .withDifferenceListener(listener);
        if (!ignoredPaths.isEmpty()) {
            asserter = asserter.whenIgnoringPaths(ignoredPaths.toArray(new String[0]));
        }
        try {
            asserter.isEqualTo(expect);
            return null;
        } catch (Throwable failure) {
            return failure;
        }
    }

    private void buildDiffLines(Object expect) {
        Tree actualTree = new Tree(actual, listener::actualDiff);
        Tree expectTree = new Tree(expect, listener::expectedDiff);
        new NodePair(actualTree.root, actualTree.nodes, expectTree.root, expectTree.nodes)
                .print(actualLines, expectedLines);
    }

    private Object parse(String json) {
        try {
            return mapper.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("cannot parse JSON: " + json, e);
        }
    }

    private String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("cannot serialize object to JSON", e);
        }
    }
}
