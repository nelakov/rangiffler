package com.elakov.rangiffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement header = $(".form__header");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement formError = $(".form__error");

    @Override
    public LoginPage checkThatPageLoaded() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Login page' was loaded", () -> {

            softstep.add("Check that header visible", () -> header.shouldHave(text("Login to Rangiffler")));
            softstep.add("Check username input visible", () -> usernameInput.should(visible));
            softstep.add("Check password input visible", () -> passwordInput.should(visible));

            softstep.execute();
            return this;
        });
    }

    @Step("Fill 'Login page' and go to 'Your travels tab'")
    public LoginPage fillLoginForm(String username, String password) {
        inputUsername(username);
        inputPassword(password);
        return this;
    }

    @Step("Input username")
    public LoginPage inputUsername(String username) {
        usernameInput.val(username);
        return this;
    }

    @Step("Input password")
    public LoginPage inputPassword(String password) {
        passwordInput.val(password);
        return this;
    }

    @Step("Tap to Submit button")
    public <T> T submit(T page) {
        submitBtn.click();
        return page;
    }

    @Step("Check the 'Auth' error message")
    public LoginPage checkErrorMessage(String expectedMessage) {
        formError.shouldHave(text(expectedMessage));
        return this;
    }

}
