package com.elakov.rangiffler.test.web.profile;

import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.annotation.test.ApiLogin;
import com.elakov.rangiffler.step.web.ProfileWebStep;
import com.elakov.rangiffler.test.TestContext;
import com.elakov.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.WEB;

@Owner(ELAKOV)
@Epic("Header component")
@Feature("Profile's photo")
@Tag(WEB)
public class ProfileErrorTest extends BaseWebTest {

    ProfileWebStep steps = new ProfileWebStep();

    @Test
    @AllureId("1018")
    @ApiLogin(user = @CreateUser)
    @DisplayName("Unsuccessfully: Profile not update. User get error message if firstname more then 50 symbols")
    public void unsuccessfullyUpdateUserFirstNameMoreThen50SymbolsTest() {
        TestContext.setFirstName(DataFakeHelper.generateName51Symbols());
        steps
                .errorMessageForFirstNameShouldBeVisible("Length of this field must be no longer than 50 characters")
                .saveBtnShouldNotVisible();

    }

    @Test
    @AllureId("1019")
    @ApiLogin(user = @CreateUser)
    @DisplayName("Unsuccessfully: Profile not update. User get error message if lastname more then 50 symbols")
    public void unsuccessfullyUpdateUserlastNameMoreThen50SymbolsTest() {
        TestContext.setSurName(DataFakeHelper.generateName51Symbols());
        steps
                .errorMessageForLastNameShouldBeVisible("Length of this field must be no longer than 50 characters")
                .saveBtnShouldNotVisible();

    }

}
