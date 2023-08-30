package com.elakov.rangiffler.model;

import com.elakov.rangiffler.data.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstname;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("friendStatus")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FriendStatus friendStatus = FriendStatus.NOT_FRIEND;

    public static UserJson fromEntity(UserEntity entity) {
        UserJson usr = new UserJson();
        byte[] avatar = entity.getAvatar();
        usr.setId(entity.getId());
        usr.setUsername(entity.getUsername());
        usr.setFirstname(entity.getFirstname());
        usr.setSurname(entity.getSurname());
        usr.setAvatar(avatar != null && avatar.length > 0 ? new String(entity.getAvatar(), StandardCharsets.UTF_8) : null);
        return usr;
    }

    public static UserJson fromEntity(UserEntity entity, FriendStatus friendStatus) {
        UserJson userJson = fromEntity(entity);
        userJson.setFriendStatus(friendStatus);
        return userJson;
    }
}