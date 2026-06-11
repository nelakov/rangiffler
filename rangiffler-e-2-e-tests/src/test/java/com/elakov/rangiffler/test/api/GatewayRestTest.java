package com.elakov.rangiffler.test.api;

import com.elakov.rangiffler.data.entity.country.CountryEntity;
import com.elakov.rangiffler.data.entity.userdata.UserEntity;
import com.elakov.rangiffler.helper.AllureSoftSteps;
import com.elakov.rangiffler.helper.comparator.JsonComparator;
import com.elakov.rangiffler.jupiter.annotation.RetryingTest;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.model.CountryJson;
import com.elakov.rangiffler.model.UserJson;
import io.grpc.StatusRuntimeException;
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

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("4001")
    @DisplayName("GET /countries returns all countries matching the DB for an authenticated user")
    @CreateUser
    void getCountriesAuthenticated(UserJson user) {
        String token = login(user.username(), user.password());

        List<CountryJson> countries = gatewayApiClient.allCountries(token);
        List<CountryEntity> fromDb = countryRepository.findAll();

        new AllureSoftSteps()
                .add("countries are returned", () -> assertThat(countries).isNotEmpty())
                .add("count matches the DB", () -> assertThat(countries).hasSameSizeAs(fromDb))
                .add("contains FJ and GE", () -> assertThat(countries).extracting(CountryJson::code).contains("FJ", "GE"))
                .execute();
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("4002")
    @DisplayName("GET /currentUser returns the JWT subject and matches the DB row")
    @CreateUser
    void getCurrentUserMatchesJwtAndDatabase(UserJson user) {
        String token = login(user.username(), user.password());

        UserJson currentUser = gatewayApiClient.currentUser(token);
        UserEntity inDb = userdataRepository.findByUsername(user.username());

        new AllureSoftSteps()
                .add("JWT subject is the user", () -> assertThat(currentUser.username()).isEqualTo(user.username()))
                .add("user row exists in the DB", () -> assertThat(inDb).isNotNull())
                .add("DB username matches", () -> assertThat(inDb.getUsername()).isEqualTo(user.username()))
                .execute();
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("4003")
    @DisplayName("GET /users returns a list for an authenticated user")
    @CreateUser
    void getAllUsersAuthenticated(UserJson user) {
        String token = login(user.username(), user.password());

        List<UserJson> users = gatewayApiClient.allUsers(token);

        new AllureSoftSteps()
                .add("a user list is returned", () -> assertThat(users).isNotNull())
                .add("list excludes the requester", () -> assertThat(users).extracting(UserJson::username).doesNotContain(user.username()))
                .execute();
    }

    @Test
    @AllureId("4004")
    @DisplayName("GET /currentUser without a valid token is rejected with 401")
    void currentUserRejectsInvalidToken() {
        int status = gatewayApiClient.currentUserStatus("not-a-valid-jwt");

        assertThat(status).isEqualTo(401);
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("4005")
    @DisplayName("GET /currentUser body matches the expected JSON (structural diff attached to Allure)")
    @CreateUser
    void currentUserBodyMatchesExpectedJson(UserJson user) {
        String token = login(user.username(), user.password());

        String body = gatewayApiClient.currentUserRaw(token);

        // The id is server-generated → ignored; everything else must match. On a
        // mismatch JsonComparator attaches a side-by-side Actual/Expect diff to Allure.
        String expected = """
                {
                  "id": "ignored",
                  "username": "%s",
                  "firstName": null,
                  "lastName": null,
                  "avatar": null,
                  "friendStatus": "NOT_FRIEND"
                }""".formatted(user.username());

        new JsonComparator()
                .assertThatJson(body)
                .ignorePaths("id")
                .equalsToJson(expected);
    }
}
