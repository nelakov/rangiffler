package com.elakov.rangiffler.api.rest.gateway;

import com.elakov.rangiffler.model.CountryJson;
import com.elakov.rangiffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import java.util.List;

public interface GatewayApi {

    @GET("/countries")
    Call<List<CountryJson>> allCountries(@Header("Authorization") String bearerToken);

    @GET("/currentUser")
    Call<UserJson> currentUser(@Header("Authorization") String bearerToken);

    @GET("/users")
    Call<List<UserJson>> allUsers(@Header("Authorization") String bearerToken);
}
