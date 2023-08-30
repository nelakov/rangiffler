package com.elakov.rangiffler.api.grpc;

import com.elakov.grpc.rangiffler.grpc.Photo;
import com.elakov.grpc.rangiffler.grpc.PhotoArray;
import com.elakov.grpc.rangiffler.grpc.PhotoID;
import com.elakov.grpc.rangiffler.grpc.RangifflerPhotoServiceGrpc;
import com.elakov.rangiffler.config.services.ServicesProperties;
import com.elakov.rangiffler.model.PhotoJson;

//TODO: Настроить логирование для gRPC
public class PhotoGrpcClient extends BaseGrpcClient {
    private RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub photoServiceBlockingStub;

    public PhotoGrpcClient() {
        super(ServicesProperties.PHOTO_GRPC_HOST, ServicesProperties.PHOTO_GRPC_PORT);
        photoServiceBlockingStub = RangifflerPhotoServiceGrpc.newBlockingStub(channel);
    }

    public Photo addPhoto(Photo photo) {
        return photoServiceBlockingStub.addPhoto(photo);
    }

    public PhotoJson editPhoto(PhotoJson photoJson) {
        Photo photo = photoServiceBlockingStub.editPhoto(photoJson.toGrpcMessage());
        return PhotoJson.fromGrpcMessage(photo);
    }

    public PhotoArray getUserPhotos(String Username) {
        return photoServiceBlockingStub.getPhotosForUser(getUsername(Username));
    }

    private PhotoID getPhotoIdRequest(String id) {
        return PhotoID.newBuilder().setId(id).build();
    }

    public PhotoArray getAllFriendsPhotos(String Username) {
        return photoServiceBlockingStub.getAllFriendsPhoto(getUsername(Username));
    }

    public void deletePhoto(PhotoID photoIdRequest) {
        photoServiceBlockingStub.deletePhoto(photoIdRequest);
    }

}
