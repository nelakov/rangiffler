package com.elakov.rangiffler.test.web.auth;

import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.model.UserJson;
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
@Feature("Login")
@Tags({@Tag(WEB), @Tag(AUTH)})
public class LoginTest extends BaseWebTest {

    AuthWebStep steps = new AuthWebStep();

    @BeforeEach
    void setUp() {
        steps.openStartPageAndRedirectToLogin();
    }

    @Test
    @AllureId("1000")
    @DisplayName("Successfully: Create new user and login")
    void successfullyCreateNewUserAndLoginTest(@CreateUser UserJson user) {
        steps.fillLoginFormAndRedirectToTravelsTab(user);
    }

    @Test
    @AllureId("1001")
    @DisplayName("Successfully: user hasn't account. Redirect to registration via Sign up")
    void successfullyUserHasntAccountTest() {
        steps.clickSignUpAndRedirectToRegister();
    }

}
