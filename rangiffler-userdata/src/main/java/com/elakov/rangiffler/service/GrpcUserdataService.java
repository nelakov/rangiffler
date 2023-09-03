package com.elakov.rangiffler.service;

import com.elakov.grpc.rangiffler.grpc.RangifflerUserdataServiceGrpc;
import com.elakov.grpc.rangiffler.grpc.User;
import com.elakov.grpc.rangiffler.grpc.UserArray;
import com.elakov.grpc.rangiffler.grpc.Username;
import com.elakov.rangiffler.data.UserEntity;
import com.elakov.rangiffler.data.repository.UserRepository;
import com.elakov.rangiffler.exception.NotFoundException;
import com.elakov.rangiffler.model.FriendStatus;
import com.elakov.rangiffler.model.UserJson;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class GrpcUserdataService extends RangifflerUserdataServiceGrpc.RangifflerUserdataServiceImplBase {
    private final UserRepository userRepository;

    @Autowired
    public GrpcUserdataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void getAllFriends(Username request, StreamObserver<UserArray> responseObserver) {
        UserEntity userEntity = getUserEntityByUsername(request.getUsername());
        List<UserJson> friends = getNonPendingFriends(userEntity);
        UserArray usersResponse = buildUserArrayResponse(friends);

        responseObserver.onNext(usersResponse);
        responseObserver.onCompleted();
    }

    private UserEntity getUserEntityByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new NotFoundException("Can't find user by username: " + username);
        }
        return userEntity;
    }

    private List<UserJson> getNonPendingFriends(UserEntity userEntity) {
        return userEntity.getFriends().stream()
                .filter(fe -> !fe.isPending())
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending() ? FriendStatus.INVITATION_SENT : FriendStatus.FRIEND))
                .collect(Collectors.toList());
    }

    private UserArray buildUserArrayResponse(List<UserJson> friends) {
        List<User> usersResponse = friends.stream()
                .map(this::buildUserResponse)
                .collect(Collectors.toList());

        return UserArray.newBuilder().addAllUsers(usersResponse).build();
    }

    private User buildUserResponse(UserJson friend) {
        return User.newBuilder()
                .setId(friend.getId().toString())
                .setUsername(friend.getUsername())
                .setFirstname(friend.getFirstname() == null ? "" : friend.getFirstname())
                .setSurname(friend.getLastName() == null ? "" : friend.getLastName())
                .setAvatar(friend.getAvatar() == null ? "" : friend.getAvatar())
                .build();
    }
}
