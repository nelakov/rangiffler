package com.elakov.rangiffler.helper.comparator;

import com.google.common.base.Function;
import net.javacrumbs.jsonunit.core.listener.Difference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.addComma;
import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.closeLine;
import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.emptyContainerLine;
import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.openLine;
import static com.elakov.rangiffler.helper.comparator.JsonLineFormatter.simpleLine;

class Node {

    NodeType type = NodeType.SIMPLE;
    final Object value;
    final String name;
    List<Node> children;
    final String path;
    final int depth;
    int lineCount;
    boolean bright;

    public Node(Object obj, String path, String name, Map<String, Node> nodes, int depth, Function<String, Difference> getDiff) {
        value = obj;
        this.path = path;
        this.depth = depth;
        this.name = name;

        nodes.put(path, this);

        if (obj instanceof Map) {
            var map = (Map<String, Object>) obj;
            type = NodeType.OBJECT;
            children = new ArrayList<>();
            if (path != null && !path.isEmpty()) {
                path = path + ".";
            }
            String finalPath = path;
            map.forEach((k, v) -> children.add(new Node(v, finalPath + k, k, nodes, depth + 1, getDiff)));

        } else if (obj instanceof List) {
            var list = (List<Object>) obj;
            type = NodeType.ARRAY;
            children = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                children.add(new Node(list.get(i), path + "[" + i + "]", "", nodes, depth + 1, getDiff));
            }
        }

        Difference diff = getDiff.apply(this.path);
        if (diff != null) {
            setBright();
        }
    }

    private void setBright() {
        bright = true;
        if (children != null) {
            children.forEach(c -> c.setBright());
        }
    }

    public int calculateLineCount() {
        lineCount = 1;
        if (type != NodeType.SIMPLE && !children.isEmpty()) {
            lineCount += 1;
            children.forEach(ch -> lineCount += ch.calculateLineCount());
        }
        return lineCount;
    }

    public void print(List<Line> text) {
        if (type == NodeType.SIMPLE) {
            text.add(simpleLine(depth, name, value, bright));
        } else if (children != null && !children.isEmpty()) {
            text.add(openLine(depth, name, type, bright));
            boolean first = true;
            for (Node child : children) {
                if (!first) {
                    addComma(text);
                }
                child.print(text);
                first = false;
            }
            text.add(closeLine(depth, type, bright));
        } else {
            text.add(emptyContainerLine(depth, name, type, bright));
        }
    }
}
