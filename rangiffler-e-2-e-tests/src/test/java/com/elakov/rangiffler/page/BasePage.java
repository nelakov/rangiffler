package com.elakov.rangiffler.page;

import com.codeborne.selenide.Selenide;

public abstract class BasePage<T extends BasePage> {

    public abstract T checkThatPageLoaded();

    public T refreshPage() {
        Selenide.refresh();
        return (T) this;
    }

}
