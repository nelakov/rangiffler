package com.elakov.rangiffler.test.web.profile;

import com.elakov.rangiffler.step.web.ProfileWebStep;
import com.elakov.rangiffler.test.web.BaseWebTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;

@Owner(ELAKOV)
@Epic("Header component")
@Feature("Add photo to Profile")
public class ProfileTest extends BaseWebTest {

    ProfileWebStep steps = new ProfileWebStep();

//    @AllureId("1013")
//    @ApiLogin(
//            user = @CreateUser(
//                    friends = @CreateFriend(
//                            photos = @CreatePhoto(
//                                    photoPath = "images/place/georgia/1.jpg",
//                                    countryCode = "GE",
//                                    description = "Beautiful Georgia 1"
//                            )
//                    ),
//                    avatarPath = "images/profile/avatar_1.jpg"
//            )
//    )
//    @Test
//    @DisplayName("Add info to profile")
//    void shouldFillProfileWithAllFieldsSet(UserJson userJson) {
//        steps.fillAllFieldsAndAddPhotoToProfile();
//    }

/*    @Test
    @AllureId("3002")
    @DisplayName("WEB: User should be able update all fields in the profile")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(firstname = "Test firstname", lastname = "Test lastname"))
    void shouldUpdateProfileWithAllFieldsSet(@User UserGrpc user) {
        String newFirstname = generateRandomFirstname();
        String newLastname = generateRandomLastname();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openProfilePopup()
                .setFirstname(newFirstname)
                .setLastname(newLastname)
                .saveProfile();

        Selenide.refresh();

        mainPage.getHeader()
                .openProfilePopup()
                .checkFirstname(newFirstname)
                .checkLastname(newLastname);
    }

    @Test
    @AllureId("3003")
    @DisplayName("WEB: User should be able add an avatar in the profile")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser)
    void shouldAddAvatarIntoProfile(@User UserGrpc user) {
        String avatarPath = "img/avatar/avatar.jpg";

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openProfilePopup()
                .updateAvatar(avatarPath)
                .saveProfile();

        Selenide.refresh();

        mainPage.getHeader()
                .openProfilePopup()
                .checkAvatar(avatarPath);
    }

    @Test
    @AllureId("3004")
    @DisplayName("WEB: User should be able update an avatar in the profile")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(avatarPath = "img/avatar/dartWader.jpg"))
    void shouldUpdateAvatarInProfile(@User UserGrpc user) {
        String avatarPath = "img/avatar/avatar.jpg";

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openProfilePopup()
                .updateAvatar(avatarPath)
                .saveProfile();

        Selenide.refresh();

        mainPage.getHeader()
                .openProfilePopup()
                .checkAvatar(avatarPath);
    }*/

}
