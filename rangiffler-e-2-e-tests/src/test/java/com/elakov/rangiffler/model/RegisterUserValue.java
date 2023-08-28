package com.elakov.rangiffler.model;

public record RegisterUserValue(String username, String password, String passwordSubmit) {
    RegisterUserValue newUser(String username, String password, String passwordSubmit) {
        return new RegisterUserValue(username, password, passwordSubmit);
    }

}
