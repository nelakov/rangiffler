package com.elakov.rangiffler.helper.comparator;

import com.elakov.rangiffler.helper.comparator.listeners.DiffResultListener;
import com.elakov.rangiffler.helper.comparator.report.html.DiffReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Characterization test pinning the final rendered diff HTML for representative actual/expected
 * pairs. It captures {@link DiffReport#asHtml} — the exact string attached to Allure — because the
 * comparator's only observable output is that HTML. The intermediate Line/Text representation is an
 * implementation detail the rendering refactor is allowed to change; the HTML is not.
 *
 * <p>Golden is committed at src/test/resources/comparator/render-golden.txt. First run records it
 * and fails (re-run to verify). Any later run that changes the HTML turns this red.
 */
class ComparatorRenderingCharacterizationTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Path GOLDEN = Path.of("src/test/resources/comparator/render-golden.txt");

    private static final String[][] CASES = {
            {"{\"a\":1}", "{\"a\":2}"},
            {"{\"a\":1,\"b\":[1,2,3]}", "{\"a\":1,\"b\":[1,9,3]}"},
            {"{\"x\":{\"y\":\"hello\"}}", "{\"x\":{\"y\":\"world\"}}"},
            {"[1,2,3]", "[1,2]"},
            {"{\"a\":1,\"b\":2}", "{\"a\":1}"},
            {"{}", "{\"a\":1}"},
            {"{\"s\":\"a<b&c\"}", "{\"s\":\"x\"}"},
            {"{\"a\":1,\"b\":{\"c\":2,\"d\":[3,4]}}", "{\"a\":9,\"b\":{\"c\":2,\"d\":[3,5]}}"},
    };

    @Test
    void htmlRenderingUnchanged() throws Exception {
        StringBuilder out = new StringBuilder();
        for (String[] testCase : CASES) {
            out.append("=== ").append(testCase[0]).append(" vs ").append(testCase[1]).append(" ===\n");
            out.append(renderHtml(testCase[0], testCase[1])).append("\n\n");
        }
        String actual = out.toString();

        if (!Files.exists(GOLDEN)) {
            Files.createDirectories(GOLDEN.getParent());
            Files.writeString(GOLDEN, actual);
            throw new AssertionError("Golden recorded at " + GOLDEN + " — re-run to verify against it.");
        }
        assertThat(actual).isEqualTo(Files.readString(GOLDEN));
    }

    private String renderHtml(String actualJson, String expectJson) throws Exception {
        Object actual = MAPPER.readValue(actualJson, Object.class);
        Object expect = MAPPER.readValue(expectJson, Object.class);

        DiffResultListener listener = new DiffResultListener();
        try {
            JsonAssertions.assertThatJson(actual).withDifferenceListener(listener).isEqualTo(expect);
        } catch (AssertionError ignored) {
            // mismatch expected; we only want the collected diffs for rendering
        }

        Tree actualTree = new Tree(actual, listener::actualDiff);
        Tree expectTree = new Tree(expect, listener::expectedDiff);
        List<Line> actualLines = new ArrayList<>();
        List<Line> expectedLines = new ArrayList<>();
        new NodePair(actualTree.root, actualTree.nodes, expectTree.root, expectTree.nodes)
                .print(actualLines, expectedLines);

        return new DiffReport(Set.of()).asHtml(actualLines, expectedLines);
    }
}
