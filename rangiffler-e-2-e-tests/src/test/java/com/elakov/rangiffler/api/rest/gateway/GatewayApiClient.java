package com.elakov.rangiffler.api.rest.gateway;

import com.elakov.rangiffler.api.rest.BaseRestClient;
import com.elakov.rangiffler.model.CountryJson;
import com.elakov.rangiffler.model.UserJson;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static com.elakov.rangiffler.config.services.ServicesProperties.GATEWAY_BASE_URL;

/**
 * REST client for the gateway (HTTP :8080). Every call carries the OAuth2
 * Bearer token; the gateway derives the username from the JWT 'sub' claim.
 */
public class GatewayApiClient extends BaseRestClient {

    private final GatewayApi api;

    public GatewayApiClient() {
        super(GATEWAY_BASE_URL);
        this.api = retrofit.create(GatewayApi.class);
    }

    public List<CountryJson> allCountries(String token) {
        return body(api.allCountries(bearer(token)));
    }

    public UserJson currentUser(String token) {
        return body(api.currentUser(bearer(token)));
    }

    public List<UserJson> allUsers(String token) {
        return body(api.allUsers(bearer(token)));
    }

    /** Raw HTTP status for negative/security cases (e.g. missing/invalid token). */
    public int currentUserStatus(String token) {
        return code(api.currentUser(bearer(token)));
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }

    private static <T> T body(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                throw new IllegalStateException("Gateway returned HTTP " + response.code());
            }
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException("Gateway call failed", e);
        }
    }

    private static <T> int code(Call<T> call) {
        try {
            return call.execute().code();
        } catch (IOException e) {
            throw new RuntimeException("Gateway call failed", e);
        }
    }
}
