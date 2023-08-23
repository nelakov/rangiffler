package com.elakov.rangiffler.page.auth;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.elakov.rangiffler.config.services.ServicesProperties.CLIENT_BASE_URL;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class StartPage extends BasePage<StartPage> {
    public static final String URL = CLIENT_BASE_URL;
    private final SelenideElement header = $("div h1");
    private final SelenideElement loginBtn = $x("//a[text()='Login']");
    private final SelenideElement registerBtn = $x("//a[text()='Register']");

    @Override
    @Step("Check that 'Start page' was loaded")
    public StartPage checkThatPageLoaded() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Login page' was loaded", () -> {
            softstep.add("Check that header visible", () -> header.shouldHave(text("Be like Rangiffler")));
            softstep.add("Check username input visible", () -> loginBtn.should(visible));
            softstep.add("Check password input visible", () -> registerBtn.should(visible));
            softstep.execute();
            return this;
        });
    }

    @Step("Open Start page")
    public StartPage open() {
        Selenide.open(URL);
        return new StartPage();
    }

    @Step("Redirect to login page")
    public LoginPage openLoginPage() {
        loginBtn.click();
        return new LoginPage();
    }

    @Step("Redirect to register page")
    public RegistrationPage openRegisttrationPage() {
        registerBtn.click();
        return new RegistrationPage();
    }

}
