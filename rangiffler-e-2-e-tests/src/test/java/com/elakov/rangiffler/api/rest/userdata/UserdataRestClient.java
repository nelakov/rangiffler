package com.elakov.rangiffler.api.rest.userdata;

import com.elakov.rangiffler.api.rest.BaseRestClient;
import com.elakov.rangiffler.model.FriendJson;
import com.elakov.rangiffler.model.UserJson;
import io.restassured.http.ContentType;

import java.util.Arrays;
import java.util.List;

public class UserdataRestClient extends BaseRestClient {

    public UserdataRestClient() {
        super(CFG.userdataBaseUrl());
    }

    public UserJson currentUser(String username) {
        return asOrNull(spec()
                .queryParam("username", username)
                .get("/currentUser"), UserJson.class);
    }

    public UserJson addFriend(String username, String friendUsername) {
        return asOrNull(spec()
                .queryParam("username", username)
                .contentType(ContentType.JSON)
                .body(new FriendJson(friendUsername))
                .post("/addFriend"), UserJson.class);
    }

    public UserJson acceptInvitation(String username, String inviteUsername) {
        return asOrNull(spec()
                .queryParam("username", username)
                .contentType(ContentType.JSON)
                .body(new FriendJson(inviteUsername))
                .post("/acceptInvitation"), UserJson.class);
    }

    public UserJson updateUserInfo(UserJson userJson) {
        return asOrNull(spec()
                .contentType(ContentType.JSON)
                .body(userJson)
                .patch("/updateUserInfo"), UserJson.class);
    }

    public List<UserJson> allUsers(String username) {
        UserJson[] users = asOrNull(spec()
                .queryParam("username", username)
                .get("/allUsers"), UserJson[].class);
        return users == null ? List.of() : Arrays.asList(users);
    }
}
