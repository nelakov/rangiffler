package com.elakov.rangiffler.test.api;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.Photo;
import com.elakov.grpc.rangiffler.grpc.PhotoArray;
import com.elakov.grpc.rangiffler.grpc.PhotoID;
import com.elakov.rangiffler.data.entity.photo.PhotoEntity;
import com.elakov.rangiffler.jupiter.annotation.RetryingTest;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateFriend;
import com.elakov.rangiffler.jupiter.annotation.creation.CreatePhoto;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.model.PhotoJson;
import com.elakov.rangiffler.model.UserJson;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;

import java.util.List;
import java.util.UUID;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.API;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.DB;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.PHOTO;
import static com.elakov.rangiffler.helper.data.FileLoaderHelper.getFileByClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Owner(ELAKOV)
@Epic("Photo service")
@Feature("Photos (gRPC)")
@Tags({@Tag(API), @Tag(PHOTO), @Tag(DB)})
@DisplayName("[grpc] Photo")
class PhotoGrpcTest extends BaseGrpcTest {

    private static final String GEORGIA_PHOTO = "images/place/georgia/2.jpeg";

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3001")
    @DisplayName("addPhoto stores the photo and persists it in the DB")
    @CreateUser
    void addPhotoPersists(UserJson user) {
        Country ge = countryGrpcClient.getCountryByCode("GE");
        Photo request = Photo.newBuilder()
                .setUsername(user.username())
                .setPhoto(getFileByClasspath(GEORGIA_PHOTO))
                .setDescription("Tbilisi")
                .setCountryCode(ge)
                .build();

        Photo response = photoGrpcClient.addPhoto(request);

        assertThat(response.getId()).isNotBlank();
        assertThat(response.getUsername()).isEqualTo(user.username());
        assertThat(response.getCountryCode().getCode()).isEqualTo("GE");

        PhotoEntity inDb = photoRepository.findByUsername(user.username());
        assertThat(inDb).isNotNull();
        assertThat(inDb.getCountryCode()).isEqualTo("GE");
        assertThat(inDb.getDescription()).isEqualTo("Tbilisi");
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3002")
    @DisplayName("getPhotosForUser returns the user's photo matching the DB")
    @CreateUser(photos = @CreatePhoto(photoPath = GEORGIA_PHOTO, countryCode = "GE", description = "Georgia"))
    void getPhotosForUserMatchesDatabase(UserJson user) {
        PhotoArray photos = photoGrpcClient.getUserPhotos(user.username());

        assertThat(photos.getPhotoArrayList()).hasSize(1);
        assertThat(photos.getPhotoArray(0).getCountryCode().getCode()).isEqualTo("GE");
        assertThat(photos.getPhotoArray(0).getDescription()).isEqualTo("Georgia");

        List<PhotoEntity> inDb = photoRepository.findAllByUsername(user.username());
        assertThat(inDb).hasSize(1);
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3003")
    @DisplayName("editPhoto updates the description in the response and the DB")
    @CreateUser(photos = @CreatePhoto(photoPath = GEORGIA_PHOTO, countryCode = "GE", description = "old"))
    void editPhotoUpdatesDescription(UserJson user) {
        PhotoJson original = user.photos().getFirst();
        PhotoJson edited = new PhotoJson(
                original.id(), original.countryJson(), original.photo(),
                "updated description", user.username(), original.photoClassPath());

        PhotoJson response = photoGrpcClient.editPhoto(edited);

        assertThat(response.description()).isEqualTo("updated description");
        PhotoEntity inDb = photoRepository.findById(original.id());
        assertThat(inDb.getDescription()).isEqualTo("updated description");
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3004")
    @DisplayName("deletePhoto removes the photo from the service and the DB")
    @CreateUser(photos = @CreatePhoto(photoPath = GEORGIA_PHOTO, countryCode = "GE", description = "to delete"))
    void deletePhotoRemovesIt(UserJson user) {
        UUID photoId = user.photos().getFirst().id();

        photoGrpcClient.deletePhoto(PhotoID.newBuilder().setId(photoId.toString()).build());

        assertThat(photoGrpcClient.getUserPhotos(user.username()).getPhotoArrayList()).isEmpty();
        assertThat(photoRepository.findAllByUsername(user.username())).isEmpty();
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3005")
    @DisplayName("getAllFriendsPhoto returns photos of the user's friends")
    @CreateUser(friends = @CreateFriend(
            photos = @CreatePhoto(photoPath = GEORGIA_PHOTO, countryCode = "GE", description = "friend trip")))
    void getAllFriendsPhotoReturnsFriendPhotos(UserJson user) {
        PhotoArray friendsPhotos = photoGrpcClient.getAllFriendsPhotos(user.username());

        assertThat(friendsPhotos.getPhotoArrayList()).hasSize(1);
        assertThat(friendsPhotos.getPhotoArray(0).getDescription()).isEqualTo("friend trip");
        assertThat(friendsPhotos.getPhotoArray(0).getCountryCode().getCode()).isEqualTo("GE");
    }
}
