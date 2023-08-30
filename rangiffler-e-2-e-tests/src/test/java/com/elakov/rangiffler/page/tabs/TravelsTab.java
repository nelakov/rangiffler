package com.elakov.rangiffler.page.tabs;

import com.elakov.rangiffler.command.CommandExecutor;
import com.elakov.rangiffler.command.Refresh;
import com.elakov.rangiffler.config.services.ServicesProperties;
import com.elakov.rangiffler.page.BasePage;
import com.elakov.rangiffler.page.component.Header;
import com.elakov.rangiffler.page.component.MapComponent;
import com.elakov.rangiffler.page.component.PhotoListComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.open;

public class TravelsTab extends BasePage<TravelsTab> {

    public static final String URL = ServicesProperties.CLIENT_BASE_URL;

    private final Header headerComponent = new Header();
    private final MapComponent mapComponent = new MapComponent();
    private final PhotoListComponent photoListComponent = new PhotoListComponent();

    @Override
    @Step("Travel tab was loaded")
    public TravelsTab checkThatPageLoaded() {
        mapComponent
                .checkThatComponentDisplayed();
        return this;
    }

    @Override
    @Step("Refresh page")
    public TravelsTab refreshPage() {
        CommandExecutor.execute(new Refresh());
        return this;
    }

    public <T> T openTravelsTab(T page) {
        open(URL, TravelsTab.class);
        return page;
    }

    public MapComponent getMap() {
        return mapComponent;
    }

    public PhotoListComponent getImagesList() {
        return photoListComponent;
    }

    public Header getHeader() {
        return headerComponent;
    }

}
