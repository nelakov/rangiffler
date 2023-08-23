package com.elakov.rangiffler.page;

public abstract class BasePage<T extends BasePage> {

    public abstract T checkThatPageLoaded();
}
