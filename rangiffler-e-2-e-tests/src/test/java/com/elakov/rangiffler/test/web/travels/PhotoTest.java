package com.elakov.rangiffler.test.web.travels;

import com.elakov.rangiffler.jupiter.annotation.creation.CreatePhoto;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.annotation.test.ApiLogin;
import com.elakov.rangiffler.model.PhotoJson;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.step.web.PhotoWebStep;
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
@Feature("Photos in Travel tabs(Your and Friends)")
@Tag(WEB)
public class PhotoTest extends BaseWebTest {

    PhotoWebStep steps = new PhotoWebStep();

    @Test
    @AllureId("1013")
    @ApiLogin(user = @CreateUser(
            photos = @CreatePhoto(photoPath = "images/place/georgia/1.jpg",
                    countryCode = "GE",
                    description = "Georgia In my Mind")))
    @DisplayName("Successfully: Add photo via login and check Photo Modal View")
    void successfullyAddTravelPhoto(UserJson userJson) {
        PhotoJson photoJson = userJson.getPhotos().get(0);

        String photoElementPath = userJson.getPhotos().get(0).getPhoto();
        String countryCode = photoJson.getCountryJson().getCode();
        String description = photoJson.getDescription();

        steps
                .photoShouldBeVisibleAfterAddingAndHasCountryAndDescription(photoElementPath, countryCode, description);

    }
}
