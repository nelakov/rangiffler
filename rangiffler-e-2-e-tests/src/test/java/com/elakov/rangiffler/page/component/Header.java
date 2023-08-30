package com.elakov.rangiffler.page.component;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BaseComponent;
import com.elakov.rangiffler.page.auth.StartPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.elakov.rangiffler.helper.allure.AllureAttachHelper.addStepParameter;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($("header"));
    }
    private final SelenideElement addPhotoBtn = $(byText("Add photo"));
    private final SelenideElement userVisitedCountriesIcon = $("div[aria-label='Your visited countries']");
    private final SelenideElement userProfileIcon = $("div[aria-label='Your photos']");
    private final SelenideElement userFriendsIcon = $("div[aria-label='Your friends']");
    private final SelenideElement logoutBtn = $("div[aria-label='Logout']");

    @Override
    public Header checkThatComponentDisplayed() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Login page' was loaded", () -> {
            softstep.add("Check that header visible", () -> self.shouldHave(text("Rangiffler")));
            softstep.add("Check username input visible", () -> logoutBtn.shouldBe(visible));
            softstep.execute();
            return this;
        });
    }

    @Step("Tap to Add photo")
    public PhotoComponent addPhoto() {
        addPhotoBtn.click();
        return new PhotoComponent();
    }

    @Step("Open Profile")
    public ProfileComponent openProfile() {
        userProfileIcon.click();
        return new ProfileComponent();
    }

    @Step("Open Friends")
    public FriendsComponent openFriendsPopup() {
        userFriendsIcon.click();
        return new FriendsComponent();
    }

    @Step("Tap to log out")
    public StartPage logout() {
        logoutBtn.click();
        return new StartPage();
    }

    @Step("Check count of friends is equal expect")
    public Header checkFriendsCountInHeader(int countFriends) {
        userFriendsIcon.shouldHave(text(String.valueOf(countFriends)));
        return this;
    }

    @Step("Check count of photo is equal expect")
    public Header checkPhotosCountInHeader(int countPhoto) {
        addStepParameter("Expected count of photo", countPhoto);
        userProfileIcon.shouldHave(text(String.valueOf(countPhoto)));
        return this;
    }

    @Step("Check count of visited country is equal expect")
    public Header checkCountriesCountInHeader(int countCountry) {
        userVisitedCountriesIcon.shouldHave(text(String.valueOf(countCountry)));
        return this;
    }

    @Step("Refresh page")
    public Header refresh() {
        Selenide.refresh();
        checkThatComponentDisplayed();
        return this;
    }

}
