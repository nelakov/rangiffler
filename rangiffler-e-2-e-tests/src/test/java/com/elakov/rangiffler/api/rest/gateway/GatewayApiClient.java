package com.elakov.rangiffler.api.rest.gateway;

import com.elakov.rangiffler.api.rest.BaseRestClient;
import com.elakov.rangiffler.model.CountryJson;
import com.elakov.rangiffler.model.UserJson;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

import static com.elakov.rangiffler.config.services.ServicesProperties.GATEWAY_BASE_URL;

/**
 * REST client for the gateway (HTTP :8080). Every call carries the OAuth2
 * Bearer token; the gateway derives the username from the JWT 'sub' claim.
 */
public class GatewayApiClient extends BaseRestClient {

    public GatewayApiClient() {
        super(GATEWAY_BASE_URL);
    }

    public List<CountryJson> allCountries(String token) {
        return Arrays.asList(ok(bearer(token).get("/countries")).as(CountryJson[].class));
    }

    public UserJson currentUser(String token) {
        return ok(bearer(token).get("/currentUser")).as(UserJson.class);
    }

    /** Raw /currentUser JSON body — for JsonComparator (structural diff in Allure). */
    public String currentUserRaw(String token) {
        return ok(bearer(token).get("/currentUser")).getBody().asString();
    }

    public List<UserJson> allUsers(String token) {
        return Arrays.asList(ok(bearer(token).get("/users")).as(UserJson[].class));
    }

    /** Raw HTTP status for negative/security cases (e.g. missing/invalid token). */
    public int currentUserStatus(String token) {
        return bearer(token).get("/currentUser").statusCode();
    }

    private io.restassured.specification.RequestSpecification bearer(String token) {
        return spec().header("Authorization", "Bearer " + token);
    }

    private static Response ok(Response response) {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Gateway returned HTTP " + response.statusCode());
        }
        return response;
    }
}
