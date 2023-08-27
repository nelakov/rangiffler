package com.elakov.rangiffler.step.web;

import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import com.elakov.rangiffler.model.UserJson;

import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class AuthWebStep extends CommonWebStep<AuthWebStep> {

    public AuthWebStep successfullyLoginAndRedirectToTravelsTab(UserJson userJson) {
        step("Fill 'Login page' and tap Sign in'",
                () -> loginPage
                        .inputUsername(userJson.getUsername())
                        .inputPassword(userJson.getPassword())
                        .signIn(travelsTab)
        );
        step("Redirect to the 'Your travels' tab is displayed",
                () -> travelsTab
                        .checkThatPageLoaded()
        );
        return this;
    }

    public AuthWebStep successfullyLogout() {
        step("Tap to logout and check redirect to 'Start page'",
                () -> travelsTab
                        .getHeader()
                        .logout()
                        .checkThatPageLoaded()
        );
        return this;
    }

    public AuthWebStep unsuccessfullyLoginAndCheckAuthError(String errorMessage) {
        String username = faker.name().name();
        String pass = String.valueOf(faker.number().randomNumber());
        step("Fill 'Login page' and check error message",
                () -> loginPage
                        .checkThatPageLoaded()
                        .inputUsername(username)
                        .inputPassword(pass)
                        .signIn(loginPage)
                        .checkErrorMessage(errorMessage)
        );
        return this;
    }

    public AuthWebStep unsuccessfullyLoginAndCheckAuthError(UserAuthEntity user, String errorMessage) {
        step("Fill 'Login page' and check error message",
                () -> loginPage
                        .checkThatPageLoaded()
                        .inputUsername(user.getUsername())
                        .inputPassword(user.getPassword())
                        .signIn(loginPage)
                        .checkErrorMessage(errorMessage)
        );
        return this;
    }

    public AuthWebStep redirectToRegistrationViaSignUpBtn() {
        loginPage
                .signUp()
                .checkThatPageLoaded();
        return this;
    }

    public AuthWebStep redirectToTravelsTabAfterApiLogin() {
        startPage.openLoginPage(travelsTab);
        return this;
    }

}
