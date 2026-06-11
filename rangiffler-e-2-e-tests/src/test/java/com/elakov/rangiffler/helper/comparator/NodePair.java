package com.elakov.rangiffler.helper.comparator;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.addComma;
import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.closeLine;
import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.emptyContainerLine;
import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.openLine;
import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.paddingLine;

class NodePair {
    int lineCount;
    final Node actual, expect;
    List<NodePair> children;

    public NodePair(Node actual, Map<String, Node> actualAllNodes, Node expect, Map<String, Node> expectAllNodes) {
        this.actual = actual;
        this.expect = expect;

        if (actual != null && expect != null) {
            if (actual.type == expect.type) {
                if (actual.type == NodeType.ARRAY || actual.type == NodeType.OBJECT) {
                    children = new ArrayList<>();
                    actual.children.forEach(ch -> children.add(new NodePair(ch, actualAllNodes, expectAllNodes.get(ch.path), expectAllNodes)));
                    expect.children.forEach(ch -> {
                        if (actualAllNodes.get(ch.path) == null) {
                            children.add(new NodePair(null, actualAllNodes, ch, expectAllNodes));
                        }
                    });
                }
            }
        }
        calculateLineCount();
    }

    public void calculateLineCount() {
        if (children == null) {
            if (actual != null && expect != null) {
                lineCount = Math.max(actual.lineCount, expect.lineCount);
            } else {
                if (actual != null) {
                    lineCount = actual.lineCount;
                } else {
                    lineCount = expect.lineCount;
                }
            }
        } else {
            lineCount = 1;
            if (!children.isEmpty()) {
                lineCount += 1;
                children.forEach(ch -> lineCount += ch.lineCount);
            }
        }
    }

    public void print(List<Line> actualText, List<Line> expectText) {
        if (children == null) {
            printSimple(actual, actualText);
            printSimple(expect, expectText);
            return;
        }

        NodeType type = actual.type;
        String name = actual.name;
        int depth = actual.depth;

        if (children.isEmpty()) {
            actualText.add(emptyContainerLine(depth, name, type, actual.bright));
            expectText.add(emptyContainerLine(depth, name, type, expect.bright));
            return;
        }

        actualText.add(openLine(depth, name, type, actual.bright));
        expectText.add(openLine(depth, name, type, expect.bright));

        boolean actualFirst = true;
        boolean expectFirst = true;
        for (NodePair child : children) {
            if (child.actual != null) {
                if (!actualFirst) {
                    addComma(actualText);
                }
                actualFirst = false;
            }
            if (child.expect != null) {
                if (!expectFirst) {
                    addComma(expectText);
                }
                expectFirst = false;
            }
            child.print(actualText, expectText);
        }

        actualText.add(closeLine(depth, type, actual.bright));
        expectText.add(closeLine(depth, type, expect.bright));
    }

    private void printSimple(Node node, List<Line> text) {
        if (node == null) {
            padTo(text, lineCount);
            return;
        }
        node.print(text);
        padTo(text, lineCount - node.lineCount);
    }

    private void padTo(List<Line> text, int paddingLines) {
        for (int i = 0; i < paddingLines; i++) {
            text.add(paddingLine());
        }
    }
}
