package com.elakov.rangiffler.test.api;

import com.elakov.rangiffler.helper.comparator.JsonComparator;
import com.elakov.rangiffler.jupiter.annotation.RetryingTest;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.model.UserJson;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.API;

@Owner(ELAKOV)
@Epic("Auth service")
@Feature("OAuth2 token")
@Tags({@Tag(API)})
@DisplayName("[rest] Auth")
class AuthRestTest extends BaseRestTest {

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("5001")
    @DisplayName("token endpoint response has the expected OAuth2 shape")
    @CreateUser
    void tokenResponseHasExpectedShape(UserJson user) {
        String tokenJson = tokenResponse(user.username(), user.password());

        // The token values are volatile → ignored; the response shape (type + scope) is asserted.
        // On a mismatch JsonComparator attaches a side-by-side Actual/Expect diff to Allure.
        String expected = """
                {
                  "token_type": "Bearer",
                  "scope": "openid"
                }""";

        new JsonComparator()
                .assertThatJson(tokenJson)
                .ignorePaths("access_token", "id_token", "refresh_token", "expires_in")
                .equalsToJson(expected);
    }
}
