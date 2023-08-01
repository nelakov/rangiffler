package com.elakov.rangiffler.data;

public record User(String name,
                   String password,
                   Boolean enabled,
                   Boolean accountNonExpired,
                   Boolean accountNonLocked,
                   Boolean credentialsNonExpired) {


}
