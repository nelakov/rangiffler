package com.elakov.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BaseComponent;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class ProfileComponent extends BaseComponent<ProfileComponent> {

    public ProfileComponent() {
        super($(".MuiAvatar-root"));
    }

    private final SelenideElement templatePhotoImg = $(".MuiAvatar-root.MuiAvatar-rounded [data-testid='PersonIcon']");

    private final SelenideElement avatarImg = self.$("img[src*='data']");
    private final SelenideElement addPhotoBtn = $(By.id("file"));
    private final SelenideElement username = $(".MuiTypography-root.MuiTypography-body2");
    private final SelenideElement firstnameInput = $("input[name='firstName']");
    private final SelenideElement lastnameInput = $("input[name='lastName']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement errorHelper = $(".MuiFormHelperText-root.Mui-error");

    @Override
    public ProfileComponent checkThatComponentDisplayed() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Profile component' was loaded", () -> {

            softstep.add("Username is visible", () -> username.shouldBe(visible));
            softstep.add("First name input is visible", () -> firstnameInput.should(visible));
            softstep.add("Last name input is visible", () -> lastnameInput.should(visible));

            softstep.execute();
            return this;
        });
    }

    @Step("Default image should be visible")
    public ProfileComponent defaultImageShouldBeVisible() {
        templatePhotoImg.shouldBe(visible);
        return this;
    }

    @Step("Add photo to Profile")
    public ProfileComponent addNewPhotoToProfile(String photoClasspath) {
        addPhotoBtn.uploadFromClasspath(photoClasspath);
        return this;
    }

    @Step("Input 'First name'")
    public ProfileComponent inputFirstName(String firstname) {
        executeJavaScript("arguments[0].value = '';", firstnameInput);
        firstnameInput.setValue(firstname);
        // TODO: Temporary solution - need fix
        executeJavaScript("arguments[0].value = '';", firstnameInput);
        firstnameInput.setValue(firstname);
        return this;
    }

    @Step("Input 'Last name'")
    public ProfileComponent inputLastName(String lastname) {
        executeJavaScript("arguments[0].value = '';", lastnameInput);
        lastnameInput.sendKeys(lastname);
        // TODO: Temporary solution - need fix
        executeJavaScript("arguments[0].value = '';", lastnameInput);
        lastnameInput.sendKeys(lastname);
        return this;
    }

    @Step("Tap on the 'Save' button")
    public void saveProfile() {
        submitBtn.click();
    }

    @Step("'Save' button should be disabled")
    public void saveBtnShouldNotBeVisible() {
        submitBtn.shouldBe(disabled);
    }

    @Step("'First name' should have inputted name")
    public ProfileComponent firstNameShouldHaveInputtedName(String firstname) {
        firstnameInput.shouldHave(value(firstname));
        return this;
    }

    @Step("'Last name' should have inputted name")
    public ProfileComponent lastNameShouldHaveInputtedName(String lastname) {
        lastnameInput.shouldHave(value(lastname));
        return this;
    }

    @Step("'First name' should not have inputted name")
    public ProfileComponent firstnameShouldBeEmpty() {
        firstnameInput.shouldBe(empty);
        return this;
    }

    @Step("'Last name' should not have inputted name")
    public ProfileComponent lastnameShouldBeEmpty() {
        lastnameInput.shouldBe(empty);
        return this;
    }

    @Step("Avatar should exist'")
    public ProfileComponent avatarShouldExist(String avatarClassPath) {
        Assertions.assertEquals(avatarClassPath, avatarImg.getAttribute("src"));
        return this;
    }

    @Step()
    public ProfileComponent errorMessageForFirstNameShouldBeDisplayed(String expectedMessage) {
                AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check that error message for 'First name' displayed", () -> {

            softstep.add("Error message visible", () -> errorHelper.shouldBe(visible));
            softstep.add("Error message contains expected text", () -> errorHelper.shouldHave(text(expectedMessage)));
            softstep.add("Error message should have red color", () -> errorHelper.shouldHave(cssValue("color", "rgba(211, 47, 47, 1)")));
            softstep.add("Save button not visible", () -> submitBtn.shouldHave(disabled));
            softstep.execute();
            return this;
        });
    }

    @Step("Check that error message for 'Last name' displayed")
    public ProfileComponent errorMessageForLastNameShouldBeDisplayed(String expectedMessage) {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check that error message for 'Last name' displayed", () -> {

            softstep.add("Error message visible", () -> errorHelper.shouldBe(visible));
            softstep.add("Error message contains expected text", () -> errorHelper.shouldHave(text(expectedMessage)));
            softstep.add("Error message should have red color", () -> errorHelper.shouldHave(cssValue("color", "rgba(211, 47, 47, 1)")));
            softstep.add("Save button not visible", () -> submitBtn.shouldHave(disabled));
            softstep.execute();
            return this;
        });
    }

}
