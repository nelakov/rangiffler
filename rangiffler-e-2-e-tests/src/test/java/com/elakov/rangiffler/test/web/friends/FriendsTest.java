package com.elakov.rangiffler.test.web.friends;


import com.elakov.rangiffler.jupiter.annotation.creation.CreateFriend;
import com.elakov.rangiffler.jupiter.annotation.creation.CreatePhoto;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.annotation.test.ApiLogin;
import com.elakov.rangiffler.model.PhotoJson;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.step.web.PhotoWebStep;
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
@Epic("Navigator tab")
@Feature("Friends travel")
@Tag(WEB)
public class FriendsTest {

    PhotoWebStep steps = new PhotoWebStep();

    @Test
    @AllureId("1019")
    @ApiLogin(user = @CreateUser(
            avatarClassPath = "images/profile/avatar_1.jpeg",
            photos = @CreatePhoto(photoPath = "images/place/georgia/4.jpeg",
                    countryCode = "GE",
                    description = "Tbilisi"),
            friends = @CreateFriend(
                    photos = @CreatePhoto(
                            photoPath = "images/place/georgia/3.jpeg",
                            countryCode = "GE",
                            description = "Georgia In my Mind"))))
    @DisplayName("Successfully: Add friends photo via api and then check photo")
    void successfullyCheckFriendsPhotoTest(UserJson userJson) {
        PhotoJson friendPhotoJson = userJson.friends().get(0).photos().get(0);

        String friendPhotoElementPath = friendPhotoJson.photo();
        String countryCode = friendPhotoJson.countryJson().code();
        String description = friendPhotoJson.description();

        steps
                .openFriendsTravels()
                .friendsPhotoShouldBeVisibleAfterAddingAndHasCountryAndDescription(friendPhotoElementPath, countryCode, description);

    }

}
