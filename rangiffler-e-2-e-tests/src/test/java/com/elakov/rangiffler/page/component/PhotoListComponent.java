package com.elakov.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.page.BaseComponent;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PhotoListComponent extends BaseComponent<PhotoListComponent> {

    private final ElementsCollection photosList = self.$$("ul.MuiImageList-root img");
    SelenideElement firstPhoto = $("img.photo__list-item");

    public PhotoListComponent() {
        super($(".MuiImageList-root.MuiImageList-standard"));
    }

    @Step("List of photos is displayed")
    @Override
    public PhotoListComponent checkThatComponentDisplayed() {
        self.shouldBe(visible);
        return this;
    }

    @Step("Check count of photos")
    public PhotoListComponent checkCountOfPhotoList(int expectedCount) {
        photosList.shouldHave(size(expectedCount));
        return this;
    }

    @Step("Photo should exist'")
    public PhotoListComponent photoShouldExist(String photoByClasspath) {
        Assertions.assertEquals(photoByClasspath, firstPhoto.getAttribute("src"));
        return this;
    }

    @Step("Click to 'Photo' from the 'Photo list'")
    public PhotoComponent clickByPhoto() {
        firstPhoto.click();
        return new PhotoComponent();
    }

    @Step("Check that the 'List of photo' is hidden")
    public void checkThatComponentIsHidden() {
        self.shouldBe(hidden);
    }

}
