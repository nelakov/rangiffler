package com.elakov.rangiffler.helper.comparator;

enum NodeType {
    OBJECT, ARRAY, SIMPLE;

    String openBrace() {
        return this == ARRAY ? "[" : "{";
    }

    String closeBrace() {
        return this == ARRAY ? "]" : "}";
    }
}
