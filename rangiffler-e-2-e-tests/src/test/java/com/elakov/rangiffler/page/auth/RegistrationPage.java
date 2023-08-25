package com.elakov.rangiffler.page.auth;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.elakov.rangiffler.config.services.ServicesProperties.AUTH_BASE_URL;
import static com.elakov.rangiffler.helper.allure.AllureAttachHelper.addStepParameter;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class RegistrationPage extends BasePage<RegistrationPage> {

    public static final String authUrl = AUTH_BASE_URL + "/register";
    private final SelenideElement header = $(".form__header");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement redirect = $("a[href*='redirect']");
    private final SelenideElement formError = $(".form__error");

    @Override
    public RegistrationPage checkThatPageLoaded() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Registration page' was loaded", () -> {

            softstep.add("Check that header visible", () -> header.shouldHave(text("Register to Rangiffler")));
            softstep.add("Check username input visible", () -> usernameInput.should(visible));
            softstep.add("Check password input visible", () -> passwordInput.should(visible));
            softstep.add("Check password submit input visible", () -> passwordSubmitInput.should(visible));

            softstep.execute();
            return this;
        });
    }

    @Step("Open the 'Register page'")
    public RegistrationPage open() {
        return Selenide.open(authUrl, RegistrationPage.class);
    }

    @Step("Fill 'Registration page' and go to 'Register Success Page'")
    public RegistrationSuccessPage successFillRegistrationForm(String username, String password, String passwordSubmit) {
        inputUsername(username);
        inputPassword(password);
        inputPasswordSubmit(passwordSubmit);
        submit();
        return new RegistrationSuccessPage();
    }

    @Step("Input username")
    public RegistrationPage inputUsername(String username) {
        addStepParameter("USERNAME", username);
        usernameInput.val(username);
        return this;
    }

    @Step("Input password")
    public RegistrationPage inputPassword(String password) {
        addStepParameter("PASSWORD", password);
        passwordInput.val(password);
        return this;
    }

    @Step("Submit password")
    public RegistrationPage inputPasswordSubmit(String password) {
        addStepParameter("PASSWORD", password);
        passwordSubmitInput.val(password);
        return this;
    }

    @Step("Tap to submit")
    public RegistrationPage submit() {
        submitBtn.click();
        return this;
    }

    @Step("Check the 'Registration' error message")
    public RegistrationPage checkErrorMessage(String expectedMessage) {
        formError.shouldHave(text(expectedMessage));
        return this;
    }

}
