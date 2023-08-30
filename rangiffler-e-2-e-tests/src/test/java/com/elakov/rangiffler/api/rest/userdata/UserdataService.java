package com.elakov.rangiffler.api.rest.userdata;

import com.elakov.rangiffler.model.FriendJson;
import com.elakov.rangiffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserdataService {
    @GET("/currentUser")
    Call<UserJson> currentUser(
            @Query("username") String username
    );

    @GET("/allUsers")
    Call<List<UserJson>> allUsers(
            @Query("username") String username
    );

    @PATCH("/updateUserInfo")
    Call<UserJson> updateUserInfo(
            @Body UserJson userJson
    );

    @POST("/friends/remove")
    Call<UserJson> removeFriend(
            @Query("username") String username,
            @Body FriendJson friendJson
    );

    @POST("/addFriend")
    Call<UserJson> addFriend(
            @Query("username") String username,
            @Body FriendJson friendJson
    );

    @POST("/acceptInvitation")
    Call<UserJson> acceptInvitation(
            @Query("username") String username,
            @Body FriendJson friendJson
    );

    @POST("friends/decline")
    Call<UserJson> declineInvitation(
            @Query("username") String username,
            @Body FriendJson friend
    );

}
