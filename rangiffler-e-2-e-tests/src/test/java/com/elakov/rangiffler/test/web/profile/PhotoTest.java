package com.elakov.rangiffler.test.web.profile;

import com.elakov.rangiffler.jupiter.annotation.creation.CreatePhoto;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.annotation.test.ApiLogin;
import com.elakov.rangiffler.model.PhotoJson;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.step.web.ProfileWebStep;
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
@Epic("Header component")
@Feature("Add photo to Profile")
@Tags({@Tag(WEB), @Tag(AUTH)})
public class PhotoTest extends BaseWebTest {

    ProfileWebStep steps = new ProfileWebStep();

    @AllureId("1013")
    @ApiLogin(
            user = @CreateUser(
                    photos = @CreatePhoto(
                            photoPath = "images/place/georgia/1.jpg",
                            countryCode = "GE",
                            description = "Beautiful Georgia 1"),
                    avatarClassPath = "images/profile/avatar_1.jpg"
            )
    )
    @Test
    @DisplayName("Add photo")
    void addPhoto(UserJson userJson) {
        // Prepare data
        PhotoJson photoJson = userJson.getPhotos().get(0);

//        String photoClassPath = photoJson.getPhotoClassPath();
        String photoClassPath = photoJson.getPhoto();
        String countryCode = photoJson.getCountryJson().getCode();
        String description = photoJson.getDescription();

        // Action
        steps
//                .addPhotoWithCountryAndDescription(photoClassPath, countryCode, description)
                //check
                .checkPhotoInfo(photoClassPath, countryCode, description);

    }
}
