package com.elakov.rangiffler.api.rest.userdata;

import com.elakov.rangiffler.api.rest.BaseRestClient;
import com.elakov.rangiffler.model.FriendJson;
import com.elakov.rangiffler.model.UserJson;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.List;

//TODO: Настроить логирование для Retrofit
public class UserdataRestClient extends BaseRestClient {

    public UserdataRestClient() {
        super(CFG.userdataBaseUrl());
    }

    UserdataService userDataService = retrofit.create(UserdataService.class);

    public UserJson currentUser(String username) {
        try {
            return userDataService.currentUser(username).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can`t execute api call to rangiffler-userdata: " + e.getMessage());
            return null;
        }
    }

    public @NonNull UserJson addFriend(String username, String friendUsername) {
        try {
            FriendJson friendJson = new FriendJson();
            friendJson.setUsername(friendUsername);
            return userDataService.addFriend(username, friendJson).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can`t execute api call to rangiffler-userdata: " + e.getMessage());
            return null;
        }
    }

    public @NonNull UserJson acceptInvitation(String username, String inviteUsername) {
        try {
            FriendJson friendJson = new FriendJson();
            friendJson.setUsername(inviteUsername);
            return userDataService.acceptInvitation(username, friendJson).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can`t execute api call to rangiffler-userdata: " + e.getMessage());
            return null;
        }
    }

    public UserJson updateUserInfo(UserJson userJson) {
        try {
            return userDataService.updateUserInfo(userJson).execute().body();
        } catch (IOException e) {
            Assertions.fail("unsuccessful connection to the service niffler-userData " + e.getMessage());
            return null;
        }
    }

    public List<UserJson> allUsers(String username) {
        try {
            return userDataService.allUsers(username).execute().body();
        } catch (IOException e) {
            Assertions.fail("Can`t execute api call to rangiffler-userdata: " + e.getMessage());
            return null;
        }
    }
}
