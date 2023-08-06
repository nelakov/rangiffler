package com.elakov.rangiffler.controller;
;
import java.util.List;
import java.util.UUID;

import com.elakov.rangiffler.model.PhotoJson;
import com.elakov.rangiffler.service.api.GrpcPhotoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {

    private final GrpcPhotoClient grpcPhotoClient;

    @Autowired
    public PhotoController(GrpcPhotoClient grpcPhotoClient) {
        this.grpcPhotoClient = grpcPhotoClient;
    }

    @GetMapping("/photos")
    public List<PhotoJson> getPhotosForUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return grpcPhotoClient.getPhotosForUser(username);
    }

    @GetMapping("/friends/photos")
    public List<PhotoJson> getAllFriendsPhotos(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return grpcPhotoClient.getAllFriendsPhotos(username);
    }

    @PostMapping("/photos")
    public PhotoJson addPhoto(@AuthenticationPrincipal Jwt principal,
                             @RequestBody PhotoJson photoDto) {
        String username = principal.getClaim("sub");
        photoDto.setUsername(username);
        return grpcPhotoClient.addPhoto(photoDto);
    }

    @PatchMapping("/photos/{id}")
    public PhotoJson editPhoto(@RequestBody PhotoJson photoDto) {
        return grpcPhotoClient.editPhoto(photoDto);
    }

    @DeleteMapping("/photos")
    public void deletePhoto(@RequestParam UUID photoId) {
        grpcPhotoClient.deletePhoto(photoId);
    }

}