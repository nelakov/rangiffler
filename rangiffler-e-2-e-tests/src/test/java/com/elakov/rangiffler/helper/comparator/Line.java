package com.elakov.rangiffler.helper.comparator;

/**
 * One rendered line: its indent depth, its text, and whether a trailing comma follows it. The comma
 * is a flag rather than part of the text so the report can place it after the highlight span.
 */
public class Line {

    private final int indentSize;
    private final Text text;
    private final boolean trailingComma;

    public Line(int indentSize, Text text) {
        this(indentSize, text, false);
    }

    private Line(int indentSize, Text text, boolean trailingComma) {
        this.indentSize = indentSize;
        this.text = text;
        this.trailingComma = trailingComma;
    }

    public int indentSize() {
        return indentSize;
    }

    public Text text() {
        return text;
    }

    public boolean hasTrailingComma() {
        return trailingComma;
    }

    public Line withTrailingComma() {
        return new Line(indentSize, text, true);
    }
}
