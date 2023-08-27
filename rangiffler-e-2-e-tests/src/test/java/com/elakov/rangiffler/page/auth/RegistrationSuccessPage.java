package com.elakov.rangiffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BasePage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class RegistrationSuccessPage extends BasePage<RegistrationSuccessPage> {

    private final SelenideElement header = $("p:nth-child(1)");
    private final SelenideElement signInBtn = $("[href='http://127.0.0.1:3001/redirect']");

    @Override
    public RegistrationSuccessPage checkThatPageLoaded() {
        return step("Check that Register success page loaded", () -> {
            header.shouldHave(exactText("Congratulations! You've registered!"));
            return this;
        });
    }

    public LoginPage signInClick() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Click to 'Sign in!' button and redirect to Login page", () -> {

            softstep.add("'Sign up' button contains text 'Sign up!' and clickable", () -> signInBtn.shouldHave(text("Sign in!")).click());

            softstep.execute();
            return new LoginPage();
        });
    }

}
