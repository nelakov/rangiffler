package com.elakov.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UserJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("username") String username,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("avatar") String avatar,
        @JsonIgnore String password,
        @JsonProperty("friendStatus") FriendStatus friendStatus,
        @JsonIgnore List<UserJson> friends,
        @JsonIgnore List<UserJson> outcomeInvitations,
        @JsonIgnore List<UserJson> incomeInvitations,
        @JsonIgnore List<PhotoJson> photos,
        @JsonIgnore String avatarClassPath
) {

    public UserJson {
        if (friends == null) friends = new ArrayList<>();
        if (outcomeInvitations == null) outcomeInvitations = new ArrayList<>();
        if (incomeInvitations == null) incomeInvitations = new ArrayList<>();
        if (photos == null) photos = new ArrayList<>();
    }

    public UserJson withPassword(String password) {
        return new UserJson(id, username, firstName, lastName, avatar, password,
                friendStatus, friends, outcomeInvitations, incomeInvitations, photos, avatarClassPath);
    }

    public UserJson withUserInfo(String firstName, String lastName, String avatar) {
        return new UserJson(id, username, firstName, lastName, avatar, password,
                friendStatus, friends, outcomeInvitations, incomeInvitations, photos, avatarClassPath);
    }

    public UserJson withAvatar(String avatar, String avatarClassPath) {
        return new UserJson(id, username, firstName, lastName, avatar, password,
                friendStatus, friends, outcomeInvitations, incomeInvitations, photos, avatarClassPath);
    }
}
