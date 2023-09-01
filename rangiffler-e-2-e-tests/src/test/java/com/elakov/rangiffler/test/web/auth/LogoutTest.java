package com.elakov.rangiffler.test.web.auth;

import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.annotation.test.ApiLogin;
import com.elakov.rangiffler.step.web.AuthWebStep;
import com.elakov.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.AUTH;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.WEB;

@Owner(ELAKOV)
@Epic("Auth service")
@Feature("Login")
@Tags({@Tag(WEB), @Tag(AUTH)})
public class LogoutTest extends BaseWebTest {

    AuthWebStep steps = new AuthWebStep();

    @ApiLogin(user = @CreateUser)
    @Test
    @Tag("WEB")
    @AllureId("1007")
    @DisplayName("Successfully: logout")
    void successfullylogoutTest() {
        steps
                .apiLoginAndRedirectToTravelsTab()
                .successfullyLogout();
    }
}
