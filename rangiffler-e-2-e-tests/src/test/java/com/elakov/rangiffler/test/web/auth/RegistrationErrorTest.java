package com.elakov.rangiffler.test.web.auth;

import com.elakov.rangiffler.model.RegisterUserValue;
import com.elakov.rangiffler.step.web.AuthWebStep;
import com.elakov.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.*;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.AUTH;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.WEB;

@Owner(ELAKOV)
@Epic("Auth service")
@Feature("Registration")
@Tags({@Tag(WEB), @Tag(AUTH)})
public class RegistrationErrorTest extends BaseWebTest {

    AuthWebStep steps = new AuthWebStep();

    @BeforeEach
    void setUp() {
        steps.openStartPageAndRedirectToRegister();
    }

    @Test
    @AllureId("1009")
    @DisplayName("Successfully : Error message 'Allowed username length should be from 3 to 50 characters' should be displayed")
    public void usernameLengthNotAllowedTest() {
        var registerUser = new RegisterUserValue("qw", "qwe", "qwe");

        steps.fillRegisterFormAndCheckErrorMessage(registerUser,
                "Allowed username length should be from 3 to 50 characters");
    }

    @Test
    @AllureId("1010")
    @DisplayName("Successfully : Error message 'Allowed password length should be from 3 to 12 characters' should be displayed")
    public void passwordLengthNotAllowedTest() {
        var registerUser = new RegisterUserValue("test", "a", "a");

        steps.fillRegisterFormAndCheckErrorMessage(registerUser,
                "Allowed password length should be from 3 to 12 characters");
    }

    @Test
    @AllureId("1011")
    @DisplayName("Successfully : Error message 'Passwords should be equal' should be displayed")
    public void passwordsShouldBeEqualsTest() {
        var registerUser = new RegisterUserValue("test", "qwert", "qwerty");

        steps.fillRegisterFormAndCheckErrorMessage(registerUser,
                "Passwords should be equal");
    }

    @Test
    @AllureId("1012")
    @DisplayName("Successfully : Few error messages should be displayed")
    public void fewErrorMessageDisplayedTest() {
        var registerUser = new RegisterUserValue("t", "q", "qw");

        steps.fillRegisterFormAndCheckFewErrorMessage(registerUser,
                "Allowed username length should be from 3 to 50 characters",
                "Passwords should be equal \n" +
                "Allowed password length should be from 3 to 12 characters \n"
                );
    }

}
