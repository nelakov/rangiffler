package com.elakov.rangiffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class RegistrationSuccessPage extends BasePage<RegistrationSuccessPage> {

    private final SelenideElement header = $("p:nth-child(1)");
    private final SelenideElement loginBtn = $x("//a[text()='Sign in!']");

    @Override
    public RegistrationSuccessPage checkThatPageLoaded() {
        return step("Check that Register success page loaded", () -> {
            header.shouldHave(text("Congratulations! You've registered!"));
            return this;
        });
    }

    @Step("Tap to login button")
    public LoginPage clickLoginButton() {
        loginBtn.click();
        return new LoginPage();
    }

}
