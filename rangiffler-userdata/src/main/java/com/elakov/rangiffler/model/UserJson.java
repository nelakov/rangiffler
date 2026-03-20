package com.elakov.rangiffler.model;

import com.elakov.rangiffler.data.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UserJson(
        @JsonProperty("id") UUID id,
        @JsonProperty("username") String username,
        @JsonProperty("firstName") String firstname,
        @JsonProperty("lastName") String lastName,
        @JsonProperty("avatar") String avatar,
        @JsonProperty("friendStatus")
        @JsonInclude(JsonInclude.Include.NON_NULL) FriendStatus friendStatus
) {

    public UserJson {
        if (friendStatus == null) friendStatus = FriendStatus.NOT_FRIEND;
    }

    public static UserJson fromEntity(UserEntity entity) {
        byte[] avatar = entity.getAvatar();
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getLastname(),
                avatar != null && avatar.length > 0 ? new String(avatar, StandardCharsets.UTF_8) : null,
                FriendStatus.NOT_FRIEND
        );
    }

    public static UserJson fromEntity(UserEntity entity, FriendStatus friendStatus) {
        byte[] avatar = entity.getAvatar();
        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstname(),
                entity.getLastname(),
                avatar != null && avatar.length > 0 ? new String(avatar, StandardCharsets.UTF_8) : null,
                friendStatus
        );
    }
}
