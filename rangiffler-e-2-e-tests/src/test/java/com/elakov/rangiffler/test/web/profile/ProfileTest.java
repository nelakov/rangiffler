package com.elakov.rangiffler.test.web.profile;

import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.annotation.test.ApiLogin;
import com.elakov.rangiffler.model.UserJson;
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
import static com.elakov.rangiffler.helper.data.FileLoaderHelper.getFileByClasspath;

@Owner(ELAKOV)
@Epic("Header component")
@Feature("Profile's photo")
@Tag(WEB)
public class ProfileTest extends BaseWebTest {

    ProfileWebStep steps = new ProfileWebStep();

    @Test
    @AllureId("1014")
    @ApiLogin(user = @CreateUser(avatarClassPath = "images/profile/avatar_1.jpg"))
    @DisplayName("Successfully: Profile's info saved after added")
    void successfullySavedProfileInfoTest(UserJson userJson) throws InterruptedException {
        String avatarClassPath = userJson.getAvatar();

        steps
                .updateProfileAndSaveWithoutUpdatePhoto()
                .checkProfileInfoAndAvatarAfterUpdate(avatarClassPath);

    }

    @Test
    @AllureId("1015")
    @ApiLogin(user = @CreateUser(
//            firstname = "Zaza",
//            lastname = "kakhetinskiy",
            avatarClassPath = "images/profile/avatar_1.jpg"))
    @DisplayName("Successfully: Profile's info saved after update")
    void successfullyUpdateProfileInfoTest(UserJson userJson) throws InterruptedException {
        String newAvatarClasspath = "images/profile/avatar_2.jpg";
        String convertClasspath = getFileByClasspath("images/profile/avatar_2.jpg");

        steps
                .updateProfileAndSaveWithoutUpdatePhoto()
                .updateProfileAndSave(newAvatarClasspath)
                .checkProfileInfoAndAvatarAfterUpdate(convertClasspath);

    }

    @Test
    @AllureId("1016")
    @ApiLogin(user = @CreateUser)
    @DisplayName("Successfully: Profile's info doesn't update without save")
    void successfullyDontSaveProfileInfo(UserJson userJson) {
        TestContext.setFirstName(DataFakeHelper.generateRandomFunnyUsername());
        TestContext.setSurName(DataFakeHelper.generateRandomSurname());
        String avatarClassPath = userJson.getAvatarClassPath();

        steps
                .updateProfileWithoutSave(avatarClassPath)
                .profileShouldBeEmpty();

    }

    @Test
    @AllureId("1017")
    @ApiLogin(user = @CreateUser)
    @DisplayName("Successfully: Profile condition doesn't change. Save button should not be active condition")
    void successfullySaveBtnShouldBeNotActiveTest() {

        steps
                .saveBtnShouldNotVisibleWithoutUpdates();

    }

}
