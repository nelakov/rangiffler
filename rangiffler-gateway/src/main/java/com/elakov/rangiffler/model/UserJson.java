package com.elakov.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("username") String username,
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("avatar") String avatar,
        @JsonProperty("friendStatus") FriendStatus friendStatus
) {

    public UserJson withUsername(String username) {
        return new UserJson(id, username, firstName, lastName, avatar, friendStatus);
    }
}
