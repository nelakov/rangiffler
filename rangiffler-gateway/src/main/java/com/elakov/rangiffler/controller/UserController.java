package com.elakov.rangiffler.controller;

import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.service.api.RestUserdataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {

    private final RestUserdataClient restUserdataClient;

    @Autowired
    public UserController(RestUserdataClient restUserdataClient) {
        this.restUserdataClient = restUserdataClient;
    }

    @PatchMapping("/currentUser")
    public UserJson updateUserInfo(@AuthenticationPrincipal Jwt principal,
                                   @Validated @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        user.setUsername(username);
        return restUserdataClient.updateUserInfo(user);
    }

    @GetMapping("/currentUser")
    public UserJson currentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserdataClient.currentUser(username);
    }

    @GetMapping("/users")
    public List<UserJson> allUsers(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restUserdataClient.getAllUsers(username);
    }
}