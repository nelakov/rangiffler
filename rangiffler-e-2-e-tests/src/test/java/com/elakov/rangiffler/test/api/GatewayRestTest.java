package com.elakov.rangiffler.test.api;

import com.elakov.rangiffler.data.entity.country.CountryEntity;
import com.elakov.rangiffler.data.entity.userdata.UserEntity;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.model.CountryJson;
import com.elakov.rangiffler.model.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.API;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.DB;
import static org.assertj.core.api.Assertions.assertThat;

@Owner(ELAKOV)
@Epic("Gateway")
@Feature("REST API (JWT)")
@Tags({@Tag(API), @Tag(DB)})
@DisplayName("[rest] Gateway")
class GatewayRestTest extends BaseRestTest {

    @Test
    @AllureId("4001")
    @DisplayName("GET /countries returns all countries matching the DB for an authenticated user")
    @CreateUser
    void getCountriesAuthenticated(UserJson user) {
        String token = login(user.username(), user.password());

        List<CountryJson> countries = gatewayApiClient.allCountries(token);
        List<CountryEntity> fromDb = countryRepository.findAll();

        assertThat(countries).isNotEmpty();
        assertThat(countries).hasSameSizeAs(fromDb);
        assertThat(countries).extracting(CountryJson::code)
                .contains("FJ", "GE");
    }

    @Test
    @AllureId("4002")
    @DisplayName("GET /currentUser returns the JWT subject and matches the DB row")
    @CreateUser
    void getCurrentUserMatchesJwtAndDatabase(UserJson user) {
        String token = login(user.username(), user.password());

        UserJson currentUser = gatewayApiClient.currentUser(token);

        assertThat(currentUser.username()).isEqualTo(user.username());
        UserEntity inDb = userdataRepository.findByUsername(user.username());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getUsername()).isEqualTo(user.username());
    }

    @Test
    @AllureId("4003")
    @DisplayName("GET /users returns a list for an authenticated user")
    @CreateUser
    void getAllUsersAuthenticated(UserJson user) {
        String token = login(user.username(), user.password());

        List<UserJson> users = gatewayApiClient.allUsers(token);

        assertThat(users).isNotNull();
        assertThat(users).extracting(UserJson::username).doesNotContain(user.username());
    }

    @Test
    @AllureId("4004")
    @DisplayName("GET /currentUser without a valid token is rejected with 401")
    void currentUserRejectsInvalidToken() {
        int status = gatewayApiClient.currentUserStatus("not-a-valid-jwt");

        assertThat(status).isEqualTo(401);
    }
}
