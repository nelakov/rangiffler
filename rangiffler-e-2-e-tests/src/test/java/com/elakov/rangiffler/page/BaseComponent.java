package com.elakov.rangiffler.page;

import com.codeborne.selenide.SelenideElement;

public abstract class BaseComponent<T> {

    protected final SelenideElement self;

    public BaseComponent(SelenideElement self) {
        this.self = self;
    }

    public abstract T checkThatComponentDisplayed();

}
