package com.elakov.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FriendJson(
        @JsonProperty("username") String username
) {
}
