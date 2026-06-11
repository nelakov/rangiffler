package com.elakov.rangiffler.helper.comparator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.html.HtmlEscapers;

import java.util.List;
import java.util.Objects;

/**
 * Builds the {@link Line}s that make up one side of the rendered diff. Centralises how a JSON line
 * looks — name prefix, braces, empty container, value serialization — so {@link Node} and
 * {@link NodePair} emit lines through one place instead of each rebuilding the strings.
 */
public class JsonLineFormatter {

    private static final String EMPTY_LINE = "&nbsp;";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Line simpleLine(int depth, String name, Object value, boolean bright) {
        return new Line(depth, new Text(namePrefix(name) + toJson(value), bright));
    }

    public static Line openLine(int depth, String name, NodeType type, boolean bright) {
        return new Line(depth, new Text(namePrefix(name) + type.openBrace(), bright));
    }

    public static Line closeLine(int depth, NodeType type, boolean bright) {
        return new Line(depth, new Text(type.closeBrace(), bright));
    }

    public static Line emptyContainerLine(int depth, String name, NodeType type, boolean bright) {
        return new Line(depth, new Text(namePrefix(name) + type.openBrace() + type.closeBrace(), bright));
    }

    public static Line paddingLine() {
        return new Line(0, new Text(EMPTY_LINE, false));
    }

    /**
     * Marks the last non-padding line with a trailing comma. Padding lines are skipped so the comma
     * lands on the real value that precedes them.
     */
    public static void addComma(List<Line> lines) {
        int i = lines.size() - 1;
        while (i >= 0 && Objects.equals(EMPTY_LINE, lines.get(i).text().content())) {
            i--;
        }
        if (i >= 0) {
            lines.set(i, lines.get(i).withTrailingComma());
        }
    }

    private static String namePrefix(String name) {
        return (name != null && !name.isEmpty()) ? "\"" + name + "\": " : "";
    }

    private static String toJson(Object obj) {
        try {
            return HtmlEscapers.htmlEscaper().escape(MAPPER.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
