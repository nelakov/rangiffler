package com.elakov.rangiffler.step.web;

import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.model.RegisterUserValue;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.test.TestContext;

import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class AuthWebStep extends CommonWebStep<AuthWebStep> {

    public AuthWebStep fillLoginFormAndRedirectToTravelsTab(UserJson userJson) {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();
        softStep.add("Fill 'Login page' and tap Sign in'",
                () -> loginPage
                        .inputUsername(userJson.getUsername())
                        .inputPassword(userJson.getPassword())
                        .signInClick(travelsTab)
                        .checkThatPageLoaded()
        );
        softStep.execute();
        return this;
    }

    //Overload
    public AuthWebStep fillLoginFormAndRedirectToTravelsTab() {
        AllureSoftStepsHelper softStep = new AllureSoftStepsHelper();

        softStep.add("Fill 'Login page' and tap Sign in'",
                () -> loginPage
                        .inputUsername(TestContext.getUsername())
                        .inputPassword(TestContext.getPassword())
                        .signInClick(travelsTab)
                        .checkThatPageLoaded()
        );
        softStep.execute();
        return this;
    }

    public AuthWebStep clickSignUpAndRedirectToRegister() {
        loginPage
                .signUpClick()
                .checkThatPageLoaded();

        return this;
    }

    public AuthWebStep fillRegisterFormAndRedirectToLogin() {
        TestContext.setUsername(faker.funnyName().name());
        TestContext.setPassword(DataFakeHelper.generateRandomPassword());

        registrationPage
                .checkThatPageLoaded()
                .inputUsername(TestContext.getUsername())
                .inputPassword(TestContext.getPassword())
                .inputPasswordSubmit(TestContext.getPassword())
                .signUpClick(registrationSuccessPage)
                .checkThatPageLoaded()
                .signInClick();
        return this;
    }

    public AuthWebStep apiLoginAndRedirectToTravelsTab() {
        startPage.openLoginPage(travelsTab);
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

    public AuthWebStep fillLoginFormAndCheckErrorMessage(String errorMessage) {
        String username = faker.name().name();
        String pass = String.valueOf(faker.number().randomNumber());
        step("Fill 'Login page' and check error message",
                () -> loginPage
                        .checkThatPageLoaded()
                        .inputUsername(username)
                        .inputPassword(pass)
                        .signInClick(loginPage)
                        .checkErrorMessage(errorMessage)
        );
        return this;
    }

    public AuthWebStep fillLoginFormAndCheckErrorMessage(UserAuthEntity user, String errorMessage) {
        step("Fill 'Login page' and check error message",
                () -> loginPage
                        .checkThatPageLoaded()
                        .inputUsername(user.getUsername())
                        .inputPassword(user.getPassword())
                        .signInClick(loginPage)
                        .checkErrorMessage(errorMessage)
        );
        return this;
    }

    public AuthWebStep fillRegisterFormAndCheckErrorMessage(RegisterUserValue registerUserValue, String errorMessage) {
        step("Fill 'Register page' and check error message",
                () -> registrationPage
                        .checkThatPageLoaded()
                        .inputUsername(registerUserValue.username())
                        .inputPassword(registerUserValue.password())
                        .inputPasswordSubmit(registerUserValue.passwordSubmit())
                        .signUpClick(registrationPage)
                        .errorMessage(errorMessage)
        );
        return this;
    }

    public AuthWebStep fillRegisterFormAndCheckFewErrorMessage(RegisterUserValue registerUserValue, String usernameError, String passwordError) {
        step("Fill 'Register page' and check error message",
                () -> registrationPage
                        .checkThatPageLoaded()
                        .inputUsername(registerUserValue.username())
                        .inputPassword(registerUserValue.password())
                        .inputPasswordSubmit(registerUserValue.passwordSubmit())
                        .signUpClick(registrationPage)
                        .usernameErrorMessage(usernameError)
                        .passwordErrorMessage(passwordError)
        );
        return this;
    }

}
