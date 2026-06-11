package com.elakov.rangiffler.helper.comparator;

import com.google.common.base.Function;
import net.javacrumbs.jsonunit.core.listener.Difference;

import java.util.HashMap;
import java.util.Map;

class Tree {
    final Node root;
    final Map<String, Node> nodes = new HashMap<>();

    public Tree(Object obj, Function<String, Difference> getDiff) {
        root = new Node(obj, "", "", nodes, 0, getDiff);
        root.calculateLineCount();
    }
}
