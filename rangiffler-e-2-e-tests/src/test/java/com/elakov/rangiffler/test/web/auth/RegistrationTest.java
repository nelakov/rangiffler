package com.elakov.rangiffler.test.web.auth;

import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import com.elakov.rangiffler.data.repository.auth.AuthRepository;
import com.elakov.rangiffler.data.repository.auth.AuthRepositoryImpl;
import com.elakov.rangiffler.step.web.AuthWebStep;
import com.elakov.rangiffler.test.TestContext;
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
public class RegistrationTest extends BaseWebTest {

    AuthWebStep steps = new AuthWebStep();
    private final AuthRepository authRepository = new AuthRepositoryImpl();

    @BeforeEach
    void setUp() {
        steps.openStartPageAndRedirectToRegister();
    }

    @AfterEach
    void cleanUp() {
        UserAuthEntity user = authRepository.getUserByUsername(TestContext.getUsername());
        authRepository.removeUser(user);
    }

    @Test
    @AllureId("1008")
    @DisplayName("Successfully: Successfully registration")
    public void successRegistrationTest() {
        steps
                .fillRegisterFormAndRedirectToLogin()
                .fillLoginFormAndRedirectToTravelsTab();
    }

}
