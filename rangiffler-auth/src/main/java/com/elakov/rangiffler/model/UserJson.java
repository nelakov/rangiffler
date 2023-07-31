package com.elakov.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//TODO: Для Кафки - потом
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJson {
    @JsonProperty("username")
    private String username;
}
