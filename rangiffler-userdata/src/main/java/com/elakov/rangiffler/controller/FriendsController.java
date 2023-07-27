package com.elakov.rangiffler.controller;

import com.elakov.rangiffler.model.FriendJson;
import com.elakov.rangiffler.model.UserJson;
import com.elakov.rangiffler.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FriendsController {

    private static final Logger LOG = LoggerFactory.getLogger(FriendsController.class);

    private final UserDataService userService;

    @Autowired
    public FriendsController(UserDataService userService) {
        this.userService = userService;
    }

    @GetMapping("/friends")
    public List<UserJson> friends(@RequestParam String username,
                                  @RequestParam boolean includePending) {
        return userService.friends(username, includePending);
    }

    @GetMapping("/invitations")
    public List<UserJson> invitations(@RequestParam String username) {
        return userService.invitations(username);
    }

    @PostMapping("/acceptInvitation")
    public List<UserJson> acceptInvitation(@RequestParam String username,
                                           @RequestBody FriendJson invitation) {
        return userService.acceptInvitation(username, invitation);
    }

    @PostMapping("/friends/decline")
    public List<UserJson> declineInvitation(@RequestParam String username,
                                            @RequestBody FriendJson invitation) {
        return userService.declineInvitation(username, invitation);
    }

    @PostMapping("/addFriend")
    public UserJson addFriend(@RequestParam String username,
                              @RequestBody FriendJson friend) {
        return userService.addFriend(username, friend);
    }

    @DeleteMapping("/friends/remove")
    public List<UserJson> removeFriend(@RequestParam String username,
                                       @RequestParam String friendUsername) {
        return userService.removeFriend(username, friendUsername);
    }
}
