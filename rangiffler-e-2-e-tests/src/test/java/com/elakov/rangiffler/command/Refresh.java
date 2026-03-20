package com.elakov.rangiffler.command;

import com.codeborne.selenide.Selenide;

public class Refresh implements Command{

    @Override
    public void perform() {
        Selenide.refresh();
    }

}
