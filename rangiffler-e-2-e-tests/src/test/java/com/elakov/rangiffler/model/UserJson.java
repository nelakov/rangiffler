package com.elakov.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("password")
    private transient String password;

    @JsonProperty("friendStatus")
    private FriendStatus friendStatus;

    private transient List<UserJson> friends = new ArrayList<>();
    private transient List<UserJson> outcomeInvitations = new ArrayList<>();
    private transient List<UserJson> incomeInvitations = new ArrayList<>();
    private transient List<PhotoJson> photos = new ArrayList<>();

}

