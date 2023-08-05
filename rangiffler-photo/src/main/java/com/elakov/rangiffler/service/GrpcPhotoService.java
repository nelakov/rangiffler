package com.elakov.rangiffler.service;

import com.elakov.grpc.rangiffler.grpc.*;
import com.elakov.rangiffler.data.PhotoEntity;
import com.elakov.rangiffler.data.repository.PhotoRepository;
import com.elakov.rangiffler.service.api.GrpcCountryClient;
import com.elakov.rangiffler.service.api.GrpcUserdataClient;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GrpcPhotoService extends RangifflerPhotoServiceGrpc.RangifflerPhotoServiceImplBase {

    private final PhotoRepository photoRepository;
    private final GrpcCountryClient grpcCountryClient;
    private final GrpcUserdataClient grpcUserdataClient;

    @Autowired
    public GrpcPhotoService(PhotoRepository photoRepository,
                            GrpcCountryClient grpcCountryClient,
                            GrpcUserdataClient grpcUserdataClient) {
        this.photoRepository = photoRepository;
        this.grpcCountryClient = grpcCountryClient;
        this.grpcUserdataClient = grpcUserdataClient;
    }

    @Override
    public void getPhotosForUser(Username usernameRequest, StreamObserver<PhotoArray> responseObserver) {
        List<PhotoEntity> photoEntities = photoRepository.findAllByUsername(usernameRequest.getUsername());
        PhotoArray photoArray = buildPhotoArrayResponse(photoEntities);
        responseObserver.onNext(photoArray);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllFriendsPhoto(Username usernameRequest, StreamObserver<PhotoArray> responseObserver) {
        UserArray friendsArray = grpcUserdataClient.friends(usernameRequest);
        List<String> friendsNamesList = friendsArray.getUsersList().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        List<PhotoEntity> friendsPhotosEntityList = photoRepository.findAllByUsernameIn(friendsNamesList);
        PhotoArray photoArray = buildPhotoArrayResponse(friendsPhotosEntityList);
        responseObserver.onNext(photoArray);
        responseObserver.onCompleted();
    }

    @Override
    public void addPhoto(Photo photoRequest, StreamObserver<Photo> responseObserver) {
        PhotoEntity photoEntity = savePhotoEntity(photoRequest);
        Photo photoResponse = buildPhotoResponse(photoEntity);
        responseObserver.onNext(photoResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void editPhoto(Photo photoRequest, StreamObserver<Photo> responseObserver) {
        PhotoEntity photoEntity = savePhotoEntity(photoRequest);
        Photo photoResponse = buildPhotoResponse(photoEntity);
        responseObserver.onNext(photoResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deletePhoto(PhotoID photoIDRequest, StreamObserver<Empty> responseObserver) {
        photoRepository.deleteById(UUID.fromString(photoIDRequest.getId()));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private PhotoEntity savePhotoEntity(Photo photo) {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setId(UUID.fromString(photo.getId()));
        photoEntity.setPhoto(photo.getPhoto().getBytes(StandardCharsets.UTF_8));
        photoEntity.setCountryCode(photo.getCountryCode().getCode());
        photoEntity.setUsername(photo.getUsername());
        photoEntity.setDescription(photo.getDescription());
        return photoRepository.save(photoEntity);
    }

    private Photo buildPhotoResponse(PhotoEntity photoEntity) {
        Country countryCode = getCountryByCode(photoEntity.getCountryCode());
        return Photo.newBuilder()
                .setId(photoEntity.getId().toString())
                .setPhoto(new String(photoEntity.getPhoto(), StandardCharsets.UTF_8))
                .setCountryCode(countryCode)
                .setDescription(photoEntity.getDescription())
                .setUsername(photoEntity.getUsername())
                .build();
    }

    private PhotoArray buildPhotoArrayResponse(List<PhotoEntity> photoEntities) {
        List<Photo> photos = photoEntities.stream()
                .map(this::buildPhotoResponse)
                .collect(Collectors.toList());

        return PhotoArray.newBuilder().addAllPhotoArray(photos).build();
    }

    private Country getCountryByCode(String countryCode) {
        CountryByCodeRequest countryByCodeRequest = CountryByCodeRequest.newBuilder()
                .setCountryCode(countryCode)
                .build();
        return grpcCountryClient.getCountryByCode(countryByCodeRequest);
    }
}