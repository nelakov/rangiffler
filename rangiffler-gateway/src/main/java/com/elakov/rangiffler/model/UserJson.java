package com.elakov.rangiffler.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJson {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("username")
  private String username;

  @JsonProperty("firstName")
  private String firstName;

  @JsonProperty("lastName")
  private String lastLame;

  @JsonProperty("avatar")
  private String avatar;

  @JsonProperty("friendStatus")
  private FriendStatus friendStatus;

}