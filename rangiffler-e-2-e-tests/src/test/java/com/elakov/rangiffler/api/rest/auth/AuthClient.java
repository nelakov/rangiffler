package com.elakov.rangiffler.api.rest.auth;

public interface AuthClient {

    void authorizePreRequest() throws Throwable;

    void login(String username, String password);

    String getToken();

    String tokenResponseRaw();

    void register(String username, String password);
}
