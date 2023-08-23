package com.elakov.rangiffler.test;

import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.page.component.Header;
import com.elakov.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginNewCreateUserWithExtensionTest extends BaseWebTest {

    @AllureId("1")
    @Test
    @CreateUser()
    @DisplayName("Success login from LandingPage")
    void positiveLogin(UserJson user) {
        startPage.open()
                .openLoginPage()
                .fillLoginForm(user.getUsername(), user.getPassword())
                .submit(new Header())
                .openProfile();
    }

}
