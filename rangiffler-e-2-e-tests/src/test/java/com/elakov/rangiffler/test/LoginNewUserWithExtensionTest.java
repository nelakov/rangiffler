package com.elakov.rangiffler.test;

import com.elakov.rangiffler.data.entity.AuthUserEntity;
import com.elakov.rangiffler.helpers.AllureHelper;
import com.elakov.rangiffler.jupiter.annotation.GenerateUserWith;
import com.elakov.rangiffler.jupiter.extension.CreateUserDBExtension;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.elakov.rangiffler.jupiter.annotation.GenerateUserWith.ClientDB.HIBERNATE;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(CreateUserDBExtension.class)
public class LoginNewUserWithExtensionTest {

    @AllureId("105")
    @Test
    void loginTest(@GenerateUserWith(
            clientDB = HIBERNATE) AuthUserEntity user) {
        AllureHelper.step("TESTTT", () -> {
            assertThat(user).isNotNull();
        });

    }
}
