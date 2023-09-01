package com.elakov.rangiffler.jupiter.callback.service;

import com.elakov.grpc.rangiffler.grpc.Country;
import com.elakov.grpc.rangiffler.grpc.PhotoArray;
import com.elakov.rangiffler.api.grpc.CountryGrpcClient;
import com.elakov.rangiffler.api.grpc.PhotoGrpcClient;
import com.elakov.rangiffler.api.rest.auth.AuthRestClient;
import com.elakov.rangiffler.api.rest.userdata.UserdataRestClient;
import com.elakov.rangiffler.data.entity.auth.Authority;
import com.elakov.rangiffler.data.entity.auth.AuthorityEntity;
import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
import com.elakov.rangiffler.helper.data.DataFakeHelper;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateFriend;
import com.elakov.rangiffler.jupiter.annotation.creation.CreatePhoto;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUserInDB;
import com.elakov.rangiffler.model.PhotoJson;
import com.elakov.rangiffler.model.UserJson;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static com.elakov.rangiffler.helper.data.FileLoaderHelper.getFileByClasspath;
import static org.hibernate.internal.util.StringHelper.isNotEmpty;

public class UserService {

    private static final AuthRestClient authClient = new AuthRestClient();
    private static final UserdataRestClient userdataClient = new UserdataRestClient();

    private final CountryGrpcClient countryGrpcClient = new CountryGrpcClient();
    private final PhotoGrpcClient photoGrpcClient = new PhotoGrpcClient();


    public UserAuthEntity createUserInDb(@Nonnull CreateUserInDB annotation) {
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUsername("".equals(annotation.username()) ? DataFakeHelper.generateRandomFunnyUsername() : annotation.username());
        userAuthEntity.setPassword("".equals(annotation.password()) ? DataFakeHelper.generateRandomPassword() : annotation.password());
        userAuthEntity.setEnabled(annotation.enabled());
        userAuthEntity.setAccountNonExpired(annotation.accountNonExpired());
        userAuthEntity.setAccountNonLocked(annotation.accountNonLocked());
        userAuthEntity.setCredentialsNonExpired(annotation.credentialsNonExpired());
        userAuthEntity.setAuthorities(Arrays.stream(Authority.values()).map(
                a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(userAuthEntity);
                    return ae;
                }
        ).toList());
        return userAuthEntity;
    }

    /**
     * This method create random user
     * username and password generated with help Faker library
     * @param annotation
     * @return
     */
    public UserJson createUserViaApi(@Nonnull CreateUser annotation) {
        UserJson user = createRandomUserViaApi();

        updateUserInfoIfPresent(annotation, user);
        addFriendsIfPresent(user, annotation.friends());
        addOutcomeInvitationsIfPresent(user, annotation.outcomeInvitations());
        addIncomeInvitationsIfPresent(user, annotation.incomeInvitations());
        addPhotoIfPresent(user, annotation.photos());
        addAvatarIfPresent(user, annotation.avatarClassPath());
        return user;
    }

    /**
     * This method update info from exist user
     * @param annotation
     * @param userJson
     */
    private void updateUserInfoIfPresent(CreateUser annotation, UserJson userJson) {
        String firstname = annotation.firstname();
        String lastname = annotation.lastname();
        String avatarPath = annotation.avatarClassPath();

        userJson.setFirstName(firstname);
        userJson.setLastName(lastname);
        if (!avatarPath.isEmpty()) {
            userJson.setAvatar(getFileByClasspath(avatarPath));
        }

        if ((!firstname.isEmpty() || (!lastname.isEmpty())) || (!avatarPath.isEmpty())) {
            userdataClient.updateUserInfo(userJson);
        }
    }

    /**
     * This method is adding travel photo for country for current user
     * @param targetUser
     * @param photos
     */
    private void addPhotoIfPresent(UserJson targetUser, CreatePhoto[] photos) {
        //TODO: Исправить. Нужно положить значение photoClaaPath в TestContext, чтобы не создавать объект модели
        PhotoJson photoJson = new PhotoJson();
        if (ArrayUtils.isNotEmpty(photos)) {
            PhotoArray.Builder photoArrayBuilder = PhotoArray.newBuilder();
            for (CreatePhoto createPhoto : photos) {
                Country country = countryGrpcClient.getCountryByCode(createPhoto.countryCode());
                com.elakov.grpc.rangiffler.grpc.Photo onePhoto =
                        com.elakov.grpc.rangiffler.grpc.Photo.newBuilder()
                                .setUsername(targetUser.getUsername())
                                .setPhoto(getFileByClasspath(createPhoto.photoPath()))
                                .setDescription(createPhoto.description())
                                .setCountryCode(country)
                                .build();
                photoArrayBuilder.addPhotoArray(onePhoto);
                com.elakov.grpc.rangiffler.grpc.Photo grpcPhotoResponse = photoGrpcClient.addPhoto(onePhoto);
                targetUser.getPhotos().add(PhotoJson.fromGrpcMessage(grpcPhotoResponse));
                targetUser.getPhotos().get(0).setPhotoClassPath(createPhoto.photoPath());
            }
        }
    }

    /**
     * This method is adding avatar to user profile if avatar path exist in annotation
     * @param userJson
     * @param avatarPath
     */
    private void addAvatarIfPresent(UserJson userJson, String avatarPath) {
        if (!avatarPath.isBlank()) {
            userJson.setAvatar(getFileByClasspath(avatarPath));
            userJson.setAvatarClassPath(avatarPath);
            userdataClient.updateUserInfo(userJson);
        }
    }

    /**
     * This method is adding user to friends if user exist
     * @param targetUser
     * @param createFriends
     */
    private void addFriendsIfPresent(UserJson targetUser, CreateFriend[] createFriends) {
        if (isNotEmpty(Arrays.toString(createFriends))) {
            for (CreateFriend createFriend : createFriends) {
                UserJson friendJson = createRandomUserViaApi();
                userdataClient.addFriend(targetUser.getUsername(), friendJson.getUsername());
                userdataClient.acceptInvitation(friendJson.getUsername(), targetUser.getUsername());
                targetUser.getFriends().add(friendJson);
            }
        }
    }

    /**
     * This method create random friend from array of Friend and sent invite to new friend
     *
     * @param targetUser         - current user
     * @param outcomeInvitations - sent invite to friend
     */
    private void addOutcomeInvitationsIfPresent(UserJson targetUser, CreateFriend[] outcomeInvitations) {
        if (isNotEmpty(Arrays.toString(outcomeInvitations))) {
            for (CreateFriend oi : outcomeInvitations) {
                UserJson friendJson = createRandomUserViaApi();
                userdataClient.addFriend(targetUser.getUsername(), friendJson.getUsername());
                targetUser.getOutcomeInvitations().add(friendJson);
            }
        }
    }

    /**
     * This method create random friend from array of Friend and sent invite from friend to target user
     *
     * @param targetUser
     * @param incomeInvitations
     */
    private void addIncomeInvitationsIfPresent(UserJson targetUser, CreateFriend[] incomeInvitations) {
        if (isNotEmpty(Arrays.toString(incomeInvitations))) {
            for (CreateFriend ii : incomeInvitations) {
                UserJson friendJson = createRandomUserViaApi();
                userdataClient.addFriend(friendJson.getUsername(), targetUser.getUsername());
                targetUser.getIncomeInvitations().add(friendJson);
            }
        }
    }

    /**
     * The method create random user via api
     *
     * @return
     */
    private UserJson createRandomUserViaApi() {
        final String username = DataFakeHelper.generateRandomUsername();
        final String password = DataFakeHelper.generateRandomPassword();
        authClient.register(username, password);

        UserJson user = userdataClient.currentUser(username);
        user.setPassword(password);
        return user;
    }

}
