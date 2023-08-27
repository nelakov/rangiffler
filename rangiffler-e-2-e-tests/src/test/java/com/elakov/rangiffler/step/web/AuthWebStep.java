package com.elakov.rangiffler.step.web;

import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.test.TestContext;

import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class AuthWebStep extends CommonWebStep<AuthWebStep> {

    public AuthWebStep successfullyLoginAndRedirectToTravelsTab(UserJson userJson) {
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
    public AuthWebStep successfullyLoginAndRedirectToTravelsTab() {
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

    public AuthWebStep redirectToRegistrationViaSignUpBtn() {
        loginPage
                .signUpClick()
                .checkThatPageLoaded();

        return this;
    }

    public AuthWebStep successfullyRegistrationAndRedirectToLoginPage() {
        TestContext.setUsername(faker.funnyName().name());
        TestContext.setPassword(DataFakeHelper.generateRandomPassword());

        registrationPage
                .checkThatPageLoaded()
                .inputUsername(TestContext.getUsername())
                .inputPassword(TestContext.getPassword())
                .inputPasswordSubmit(TestContext.getPassword())
                .signUpClick()
                .checkThatPageLoaded()
                .signInClick();

        return this;
    }

    public AuthWebStep redirectToTravelsTabAfterApiLogin() {
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

    public AuthWebStep unsuccessfullyLoginAndCheckAuthError(String errorMessage) {
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

    public AuthWebStep unsuccessfullyLoginAndCheckAuthError(UserAuthEntity user, String errorMessage) {
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


}
