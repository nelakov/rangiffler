package com.elakov.rangiffler.test.api;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.Photo;
import com.elakov.grpc.rangiffler.grpc.PhotoArray;
import com.elakov.grpc.rangiffler.grpc.PhotoID;
import com.elakov.rangiffler.data.entity.photo.PhotoEntity;
import com.elakov.rangiffler.helper.AllureSoftSteps;
import com.elakov.rangiffler.helper.comparator.JsonComparator;
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
        PhotoEntity inDb = photoRepository.findByUsername(user.username());

        new AllureSoftSteps()
                .add("response has an id", () -> assertThat(response.getId()).isNotBlank())
                .add("response username matches", () -> assertThat(response.getUsername()).isEqualTo(user.username()))
                .add("response country is GE", () -> assertThat(response.getCountryCode().getCode()).isEqualTo("GE"))
                .add("photo row exists in the DB", () -> assertThat(inDb).isNotNull())
                .add("DB country is GE", () -> assertThat(inDb.getCountryCode()).isEqualTo("GE"))
                .add("DB description is Tbilisi", () -> assertThat(inDb.getDescription()).isEqualTo("Tbilisi"))
                .execute();
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3002")
    @DisplayName("getPhotosForUser returns the user's photo matching the DB")
    @CreateUser(photos = @CreatePhoto(photoPath = GEORGIA_PHOTO, countryCode = "GE", description = "Georgia"))
    void getPhotosForUserMatchesDatabase(UserJson user) {
        PhotoArray photos = photoGrpcClient.getUserPhotos(user.username());
        List<PhotoEntity> inDb = photoRepository.findAllByUsername(user.username());

        new AllureSoftSteps()
                .add("one photo returned", () -> assertThat(photos.getPhotoArrayList()).hasSize(1))
                .add("country is GE", () -> assertThat(photos.getPhotoArray(0).getCountryCode().getCode()).isEqualTo("GE"))
                .add("description is Georgia", () -> assertThat(photos.getPhotoArray(0).getDescription()).isEqualTo("Georgia"))
                .add("one photo row in the DB", () -> assertThat(inDb).hasSize(1))
                .execute();
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
        PhotoEntity inDb = photoRepository.findById(original.id());

        new AllureSoftSteps()
                .add("response description updated", () -> assertThat(response.description()).isEqualTo("updated description"))
                .add("DB description updated", () -> assertThat(inDb.getDescription()).isEqualTo("updated description"))
                .execute();
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3004")
    @DisplayName("deletePhoto removes the photo from the service and the DB")
    @CreateUser(photos = @CreatePhoto(photoPath = GEORGIA_PHOTO, countryCode = "GE", description = "to delete"))
    void deletePhotoRemovesIt(UserJson user) {
        UUID photoId = user.photos().getFirst().id();

        photoGrpcClient.deletePhoto(PhotoID.newBuilder().setId(photoId.toString()).build());

        new AllureSoftSteps()
                .add("photo gone from the service", () -> assertThat(photoGrpcClient.getUserPhotos(user.username()).getPhotoArrayList()).isEmpty())
                .add("photo gone from the DB", () -> assertThat(photoRepository.findAllByUsername(user.username())).isEmpty())
                .execute();
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3005")
    @DisplayName("getAllFriendsPhoto returns photos of the user's friends")
    @CreateUser(friends = @CreateFriend(
            photos = @CreatePhoto(photoPath = GEORGIA_PHOTO, countryCode = "GE", description = "friend trip")))
    void getAllFriendsPhotoReturnsFriendPhotos(UserJson user) {
        PhotoArray friendsPhotos = photoGrpcClient.getAllFriendsPhotos(user.username());

        new AllureSoftSteps()
                .add("one friend photo", () -> assertThat(friendsPhotos.getPhotoArrayList()).hasSize(1))
                .add("description is 'friend trip'", () -> assertThat(friendsPhotos.getPhotoArray(0).getDescription()).isEqualTo("friend trip"))
                .add("country is GE", () -> assertThat(friendsPhotos.getPhotoArray(0).getCountryCode().getCode()).isEqualTo("GE"))
                .execute();
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("3006")
    @DisplayName("getPhotosForUser body matches the expected JSON (structural diff in Allure)")
    @CreateUser(photos = @CreatePhoto(photoPath = GEORGIA_PHOTO, countryCode = "GE", description = "Georgia"))
    void getPhotosForUserBodyMatchesExpectedJson(UserJson user) {
        PhotoArray photos = photoGrpcClient.getUserPhotos(user.username());
        PhotoJson photo = PhotoJson.fromGrpcMessage(photos.getPhotoArray(0));

        // id + raw photo bytes + country UUID are volatile → ignored; the rest is
        // compared structurally and the diff is attached to Allure on a mismatch.
        String expected = """
                {
                  "country": {"code": "GE", "name": "Georgia"},
                  "description": "Georgia",
                  "username": "%s"
                }""".formatted(user.username());

        new JsonComparator()
                .assertThatObject(photo)
                .ignorePaths("id", "photo", "country.id")
                .equalsToJson(expected);
    }
}
