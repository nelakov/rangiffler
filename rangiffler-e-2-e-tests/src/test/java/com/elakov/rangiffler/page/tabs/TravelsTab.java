package com.elakov.rangiffler.page.tabs;

import com.elakov.rangiffler.page.BasePage;
import com.elakov.rangiffler.page.component.Header;
import com.elakov.rangiffler.page.component.MapComponent;
import com.elakov.rangiffler.page.component.PhotoListComponent;

public class TravelsTab extends BasePage<TravelsTab> {

    private final Header headerComponent = new Header();
    private final MapComponent mapComponent = new MapComponent();
    private final PhotoListComponent photoListComponent = new PhotoListComponent();

    @Override
    public TravelsTab checkThatPageLoaded() {
        mapComponent.checkThatComponentDisplayed();
        return this;
    }

    public MapComponent getWorldMap() {
        return mapComponent;
    }

    public PhotoListComponent getImagesList() {
        return photoListComponent;
    }

    public Header getHeader() {
        return headerComponent;
    }

}
