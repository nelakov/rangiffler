package com.elakov.rangiffler.helper.comparator.report.style;

public class BrightTextStyle {

    private final String text;

    public BrightTextStyle(String text) {
        this.text = text;
    }

    public String decorate() {
        return "<span class='bright'>" + text + "</span>";
    }
}
