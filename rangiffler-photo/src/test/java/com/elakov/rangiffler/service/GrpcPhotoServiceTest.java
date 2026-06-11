package com.elakov.rangiffler.service;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.Photo;
import com.elakov.grpc.rangiffler.grpc.PhotoArray;
import com.elakov.grpc.rangiffler.grpc.PhotoID;
import com.elakov.grpc.rangiffler.grpc.User;
import com.elakov.grpc.rangiffler.grpc.UserArray;
import com.elakov.grpc.rangiffler.grpc.Username;
import com.elakov.rangiffler.data.PhotoEntity;
import com.elakov.rangiffler.data.repository.PhotoRepository;
import com.elakov.rangiffler.service.api.GrpcCountryClient;
import com.elakov.rangiffler.service.api.GrpcUserdataClient;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrpcPhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private GrpcCountryClient grpcCountryClient;
    @Mock
    private GrpcUserdataClient grpcUserdataClient;
    @InjectMocks
    private GrpcPhotoService grpcPhotoService;

    @Captor
    private ArgumentCaptor<PhotoEntity> photoEntityCaptor;

    private static PhotoEntity photoEntity(String username, String code) {
        PhotoEntity e = new PhotoEntity();
        e.setId(UUID.randomUUID());
        e.setUsername(username);
        e.setCountryCode(code);
        e.setDescription("trip");
        e.setPhoto("img".getBytes(StandardCharsets.UTF_8));
        return e;
    }

    private void stubCountry(String code) {
        lenient().when(grpcCountryClient.getCountryByCode(any()))
                .thenReturn(Country.newBuilder().setCode(code).setName(code).build());
    }

    @Test
    @DisplayName("getPhotosForUser streams the user's photos then completes")
    void getPhotosForUser() {
        stubCountry("FJ");
        when(photoRepository.findAllByUsername("bob")).thenReturn(List.of(photoEntity("bob", "FJ")));
        @SuppressWarnings("unchecked")
        StreamObserver<PhotoArray> observer = mock(StreamObserver.class);

        grpcPhotoService.getPhotosForUser(Username.newBuilder().setUsername("bob").build(), observer);

        ArgumentCaptor<PhotoArray> captor = ArgumentCaptor.forClass(PhotoArray.class);
        verify(observer).onNext(captor.capture());
        verify(observer).onCompleted();
        assertThat(captor.getValue().getPhotoArrayList()).hasSize(1);
        assertThat(captor.getValue().getPhotoArray(0).getUsername()).isEqualTo("bob");
    }

    @Test
    @DisplayName("addPhoto persists the entity with request fields and returns the saved photo")
    void addPhoto() {
        stubCountry("FJ");
        when(photoRepository.save(any(PhotoEntity.class))).thenAnswer(inv -> {
            PhotoEntity e = inv.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });
        @SuppressWarnings("unchecked")
        StreamObserver<Photo> observer = mock(StreamObserver.class);
        Photo request = Photo.newBuilder()
                .setUsername("bob")
                .setDescription("trip")
                .setPhoto("img")
                .setCountryCode(Country.newBuilder().setCode("FJ").build())
                .build();

        grpcPhotoService.addPhoto(request, observer);

        verify(photoRepository).save(photoEntityCaptor.capture());
        PhotoEntity saved = photoEntityCaptor.getValue();
        assertThat(saved.getUsername()).isEqualTo("bob");
        assertThat(saved.getCountryCode()).isEqualTo("FJ");
        assertThat(saved.getDescription()).isEqualTo("trip");
        verify(observer).onCompleted();
    }

    @Test
    @DisplayName("deletePhoto deletes by id and completes with Empty")
    void deletePhoto() {
        UUID id = UUID.randomUUID();
        @SuppressWarnings("unchecked")
        StreamObserver<Empty> observer = mock(StreamObserver.class);

        grpcPhotoService.deletePhoto(PhotoID.newBuilder().setId(id.toString()).build(), observer);

        verify(photoRepository).deleteById(id);
        verify(observer).onNext(Empty.getDefaultInstance());
        verify(observer).onCompleted();
    }

    @Test
    @DisplayName("getAllFriendsPhoto resolves friends then streams their photos")
    void getAllFriendsPhoto() {
        stubCountry("FJ");
        when(grpcUserdataClient.friends(any())).thenReturn(
                UserArray.newBuilder().addUsers(User.newBuilder().setUsername("alice").build()).build());
        when(photoRepository.findAllByUsernameIn(List.of("alice")))
                .thenReturn(List.of(photoEntity("alice", "FJ")));
        @SuppressWarnings("unchecked")
        StreamObserver<PhotoArray> observer = mock(StreamObserver.class);

        grpcPhotoService.getAllFriendsPhoto(Username.newBuilder().setUsername("bob").build(), observer);

        ArgumentCaptor<PhotoArray> captor = ArgumentCaptor.forClass(PhotoArray.class);
        verify(observer).onNext(captor.capture());
        verify(observer).onCompleted();
        assertThat(captor.getValue().getPhotoArrayList()).hasSize(1);
    }
}
