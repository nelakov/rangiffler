package com.elakov.rangiffler.command;

import com.codeborne.selenide.SelenideElement;

public class Scroll implements Command {

    private final SelenideElement element;

    private Scroll(SelenideElement element) {
        this.element = element;
    }

    public static Scroll to(SelenideElement element) {
        return new Scroll(element);
    }

    @Override
    public void perform() {
        element.scrollIntoView("{block:'center'}");
    }
}
