package com.elakov.rangiffler.service.api;

import com.elakov.grpc.rangiffler.grpc.*;
import com.elakov.rangiffler.model.PhotoJson;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.elakov.rangiffler.model.PhotoJson.fromGrpcMessage;

@Component
public class GrpcPhotoClient {

    @GrpcClient("grpcPhotoClient")
    private RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub rangifflerPhotoServiceBlockingStub;

    public List<PhotoJson> getPhotosForUser(String username) {
        PhotoArray photoList = rangifflerPhotoServiceBlockingStub
                .getPhotosForUser(Username.newBuilder()
                        .setUsername(username)
                        .build());

        return photoList.getPhotoArrayList()
                .stream()
                .map(PhotoJson::fromGrpcMessage)
                .collect(Collectors.toList());
    }

    public List<PhotoJson> getAllFriendsPhotos(String username) {
        PhotoArray friendsPhoto = rangifflerPhotoServiceBlockingStub
                .getAllFriendsPhoto(Username.newBuilder()
                        .setUsername(username)
                        .build());

        return friendsPhoto.getPhotoArrayList()
                .stream()
                .map(PhotoJson::fromGrpcMessage)
                .collect(Collectors.toList());
    }

    public PhotoJson addPhoto(PhotoJson photoJson) {
        Photo photo = rangifflerPhotoServiceBlockingStub.addPhoto(photoJson.toGrpcMessage());
        return fromGrpcMessage(photo);
    }

    public PhotoJson editPhoto(PhotoJson photoJson) {
        Photo photo = rangifflerPhotoServiceBlockingStub.editPhoto(photoJson.toGrpcMessage());
        return fromGrpcMessage(photo);
    }

    public void deletePhoto(UUID photoId) {
        rangifflerPhotoServiceBlockingStub
                .deletePhoto(PhotoID.newBuilder()
                        .setId(photoId.toString())
                        .build());
    }
}
