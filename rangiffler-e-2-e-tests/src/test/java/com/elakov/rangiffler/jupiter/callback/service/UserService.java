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

    public UserJson createUserViaApi(@Nonnull CreateUser annotation) {
        UserJson user = createRandomUserViaApi();

        user = updateUserInfoIfPresent(annotation, user);
        addFriendsIfPresent(user, annotation.friends());
        addOutcomeInvitationsIfPresent(user, annotation.outcomeInvitations());
        addIncomeInvitationsIfPresent(user, annotation.incomeInvitations());
        addPhotoIfPresent(user, annotation.photos());
        user = addAvatarIfPresent(user, annotation.avatarClassPath());
        return user;
    }

    private UserJson updateUserInfoIfPresent(CreateUser annotation, UserJson userJson) {
        String firstname = annotation.firstname();
        String lastname = annotation.lastname();
        String avatarPath = annotation.avatarClassPath();

        String avatar = !avatarPath.isEmpty() ? getFileByClasspath(avatarPath) : userJson.avatar();
        userJson = userJson.withUserInfo(firstname, lastname, avatar);

        if ((!firstname.isEmpty() || (!lastname.isEmpty())) || (!avatarPath.isEmpty())) {
            userdataClient.updateUserInfo(userJson);
        }
        return userJson;
    }

    private void addPhotoIfPresent(UserJson targetUser, CreatePhoto[] photos) {
        if (ArrayUtils.isNotEmpty(photos)) {
            for (CreatePhoto createPhoto : photos) {
                Country country = countryGrpcClient.getCountryByCode(createPhoto.countryCode());
                com.elakov.grpc.rangiffler.grpc.Photo onePhoto =
                        com.elakov.grpc.rangiffler.grpc.Photo.newBuilder()
                                .setUsername(targetUser.username())
                                .setPhoto(getFileByClasspath(createPhoto.photoPath()))
                                .setDescription(createPhoto.description())
                                .setCountryCode(country)
                                .build();
                com.elakov.grpc.rangiffler.grpc.Photo grpcPhotoResponse = photoGrpcClient.addPhoto(onePhoto);
                PhotoJson photoJson = PhotoJson.fromGrpcMessage(grpcPhotoResponse)
                        .withPhotoClassPath(createPhoto.photoPath());
                targetUser.photos().add(photoJson);
            }
        }
    }

    private UserJson addAvatarIfPresent(UserJson userJson, String avatarPath) {
        if (!avatarPath.isBlank()) {
            userJson = userJson.withAvatar(getFileByClasspath(avatarPath), avatarPath);
            userdataClient.updateUserInfo(userJson);
        }
        return userJson;
    }

    private void addFriendsIfPresent(UserJson targetUser, CreateFriend[] createFriends) {
        if (isNotEmpty(Arrays.toString(createFriends))) {
            for (CreateFriend createFriend : createFriends) {
                UserJson friendJson = createRandomUserViaApi();
                userdataClient.addFriend(targetUser.username(), friendJson.username());
                userdataClient.acceptInvitation(friendJson.username(), targetUser.username());
                addPhotoIfPresent(friendJson, createFriend.photos());
                targetUser.friends().add(friendJson);
            }
        }
    }

    private void addOutcomeInvitationsIfPresent(UserJson targetUser, CreateFriend[] outcomeInvitations) {
        if (isNotEmpty(Arrays.toString(outcomeInvitations))) {
            for (CreateFriend oi : outcomeInvitations) {
                UserJson friendJson = createRandomUserViaApi();
                userdataClient.addFriend(targetUser.username(), friendJson.username());
                targetUser.outcomeInvitations().add(friendJson);
            }
        }
    }

    private void addIncomeInvitationsIfPresent(UserJson targetUser, CreateFriend[] incomeInvitations) {
        if (isNotEmpty(Arrays.toString(incomeInvitations))) {
            for (CreateFriend ii : incomeInvitations) {
                UserJson friendJson = createRandomUserViaApi();
                userdataClient.addFriend(friendJson.username(), targetUser.username());
                targetUser.incomeInvitations().add(friendJson);
            }
        }
    }

    private UserJson createRandomUserViaApi() {
        final String username = DataFakeHelper.generateRandomUsername();
        final String password = DataFakeHelper.generateRandomPassword();
        authClient.register(username, password);

        UserJson user = userdataClient.currentUser(username);
        return user.withPassword(password);
    }

}
