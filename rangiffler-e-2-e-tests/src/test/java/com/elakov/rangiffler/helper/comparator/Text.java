package com.elakov.rangiffler.helper.comparator;

/**
 * Raw text of a rendered line plus whether it marks a diff. A pure value object: it stores the text
 * and the bright flag and renders nothing. HTML decoration is the report layer's job — that keeps
 * the domain free of presentation and lets a trailing comma sit outside the highlight span.
 */
public class Text {

    private final String content;
    private final boolean bright;

    public Text(String content, boolean bright) {
        this.content = content;
        this.bright = bright;
    }

    public String content() {
        return content;
    }

    public boolean isBright() {
        return bright;
    }
}
