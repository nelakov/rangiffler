package com.elakov.rangiffler.test.web.auth;

import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUserInDB;
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
public class LoginErrorTest extends BaseWebTest {

    AuthWebStep steps = new AuthWebStep();

    @BeforeEach
    void setUp() {
        steps
                .openStartPageAndRedirectToLogin();
    }

    @Test
    @AllureId("1002")
    @DisplayName("Unsuccessful: Wrong auth userdata")
    public void wrongAuthUserdataAndCheckErrorTest() {
        steps
                .unsuccessfullyLoginAndCheckAuthError("Bad credentials");
    }

    @Test
    @CreateUserInDB(enabled = false)
    @AllureId("1003")
    @DisplayName("Unsuccessful: User is disabled")
    public void userNotEnabledAndCheckErrorTest(UserAuthEntity user) {
        steps
                .unsuccessfullyLoginAndCheckAuthError(user, "User is disabled");
    }

    @Test
    @CreateUserInDB(accountNonExpired = false)
    @AllureId("1004")
    @DisplayName("Unsuccessful: User account has expired")
    public void accountExpiredtest(UserAuthEntity user) {
        steps
                .unsuccessfullyLoginAndCheckAuthError(user, "User account has expired");
    }

    @Test
    @CreateUserInDB(accountNonLocked = false)
    @AllureId("1005")
    @DisplayName("Unsuccessful: User account is locked")
    public void accountLockedTest(UserAuthEntity user) {
        steps
                .unsuccessfullyLoginAndCheckAuthError(user, "User account is locked");
    }

    @Test
    @CreateUserInDB(credentialsNonExpired = false)
    @AllureId("1006")
    @DisplayName("Unsuccessful: Bad credentials. Credentials expired")
    public void credentionalsExpiredTest(UserAuthEntity user) {
        steps
                .unsuccessfullyLoginAndCheckAuthError(user, "Bad credentials");
    }

}
