package com.elakov.rangiffler.data;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class FriendsId implements Serializable {

    private UUID user;
    private UUID friend;

}
