package com.elakov.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BaseComponent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class ProfileComponent extends BaseComponent<ProfileComponent> {

    public ProfileComponent() {
        super($(".MuiPaper-root.MuiCard-root"));
    }

    private final SelenideElement templatePhotoImg = self.$("[alt='New image']");
    private final SelenideElement addPhotoBtn = self.$(By.id("file"));
    private final SelenideElement usernameItem = self.$(byText("Username"));
    private final SelenideElement firstnameInput = self.$("input[name='firstName']");
    private final SelenideElement lastnameInput = self.$("input[name='lastName']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement errorHelper = self.$(".MuiFormHelperText-root.Mui-error");

    @Override
    public ProfileComponent checkThatComponentDisplayed() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Profile component' was loaded", () -> {

            softstep.add("Username is visible", () -> usernameItem.shouldBe(visible));
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
        firstnameInput.setValue(firstname);
        return this;
    }

    @Step("Input 'Last name'")
    public ProfileComponent inputLastName(String lastname) {
        lastnameInput.setValue(lastname);
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

    @Step()
    public ProfileComponent errorMessageForFirstNameShouldBe(String expectedMessage) {
                AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check that error message for 'First name' displayed", () -> {

            softstep.add("Error message visible", () -> errorHelper.shouldHave(visible));
            softstep.add("Error message contains expected text", () -> errorHelper.shouldHave(text(expectedMessage)));
            softstep.add("Error message should have red color", () -> errorHelper.shouldHave(cssValue("color", "#d32f2f")));
            softstep.add("Save button not visible", () -> submitBtn.shouldHave(disabled));
            softstep.execute();
            return this;
        });
    }

    @Step("Check that error message for 'Last name' displayed")
    public ProfileComponent errorMessageForLastNameShouldBe(String expectedMessage) {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check that error message for 'Last name' displayed", () -> {

            softstep.add("Error message visible", () -> errorHelper.shouldHave(visible));
            softstep.add("Error message contains expected text", () -> errorHelper.shouldHave(text(expectedMessage)));
            softstep.add("Error message should have red color", () -> errorHelper.shouldHave(cssValue("color", "#d32f2f")));
            softstep.add("Save button not visible", () -> submitBtn.shouldHave(disabled));
            softstep.execute();
            return this;
        });
    }

}
