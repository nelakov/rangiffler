package com.elakov.rangiffler.command;

import com.codeborne.selenide.Selenide;

public class Refresh implements Command{

    @Override
    public void execute() {
        Selenide.refresh();
    }

}
