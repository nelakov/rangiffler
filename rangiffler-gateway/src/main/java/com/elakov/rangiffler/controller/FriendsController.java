package com.elakov.rangiffler.controller;

import com.elakov.rangiffler.model.FriendJson;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.service.api.RestUserdataClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class FriendsController {

    private static final Logger LOG = LoggerFactory.getLogger(FriendsController.class);

    private final RestUserdataClient userDataClient;

    @Autowired
    public FriendsController(RestUserdataClient userDataClient) {
        this.userDataClient = userDataClient;
    }

    @GetMapping("/friends")
    public List<UserJson> friends(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userDataClient.friends(username);
    }

    @GetMapping("invitations")
    public List<UserJson> invitations(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return userDataClient.invitations(username);
    }

    @PostMapping("friends/submit")
    public UserJson acceptInvitation(@AuthenticationPrincipal Jwt principal,
                                           @Validated @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return userDataClient.acceptInvitation(username, friend);
    }

    @PostMapping("friends/decline")
    public UserJson declineInvitation(@AuthenticationPrincipal Jwt principal,
                                            @Validated @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return userDataClient.declineInvitation(username, friend);
    }

    @PostMapping("users/invite/")
    public UserJson sendInvitation(@AuthenticationPrincipal Jwt principal,
                              @Validated @RequestBody FriendJson friend) {
        String username = principal.getClaim("sub");
        return userDataClient.addFriend(username, friend);
    }

    @PostMapping("friends/remove")
    public UserJson removeFriend(@AuthenticationPrincipal Jwt principal,
                                 @Validated @RequestBody() UserJson friend) {
        String username = principal.getClaim("sub");
        return userDataClient.removeFriend(username, friend);
    }
}
