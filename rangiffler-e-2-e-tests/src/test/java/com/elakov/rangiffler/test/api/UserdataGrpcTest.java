package com.elakov.rangiffler.test.api;

import com.elakov.grpc.rangiffler.grpc.UserArray;
import com.elakov.grpc.rangiffler.grpc.User;
import com.elakov.rangiffler.data.entity.userdata.UserEntity;
import com.elakov.rangiffler.jupiter.annotation.RetryingTest;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateFriend;
import com.elakov.rangiffler.jupiter.annotation.creation.CreateUser;
import com.elakov.rangiffler.model.UserJson;
import io.grpc.StatusRuntimeException;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;

import static com.elakov.rangiffler.helper.allure.tags.AllureOwner.ELAKOV;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.API;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.DB;
import static com.elakov.rangiffler.helper.allure.tags.AllureTag.USERDATA;
import static org.assertj.core.api.Assertions.assertThat;

@Owner(ELAKOV)
@Epic("Userdata service")
@Feature("Friends (gRPC)")
@Tags({@Tag(API), @Tag(USERDATA), @Tag(DB)})
@DisplayName("[grpc] Userdata friends")
class UserdataGrpcTest extends BaseGrpcTest {

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("2001")
    @DisplayName("getAllFriends returns the user's accepted friend")
    @CreateUser(friends = @CreateFriend)
    void getAllFriendsReturnsAcceptedFriend(UserJson user) {
        String expectedFriend = user.friends().getFirst().username();

        UserArray friends = userdataGrpcClient.getAllFriends(user.username());

        assertThat(friends.getUsersList())
                .extracting(User::getUsername)
                .containsExactly(expectedFriend);

        // DB cross-check: the friend row really exists in userdata
        UserEntity friendInDb = userdataRepository.findByUsername(expectedFriend);
        assertThat(friendInDb).isNotNull();
        assertThat(friendInDb.getUsername()).isEqualTo(expectedFriend);
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("2002")
    @DisplayName("getAllFriends returns empty for a user with no friends")
    @CreateUser
    void getAllFriendsEmptyForLonelyUser(UserJson user) {
        UserArray friends = userdataGrpcClient.getAllFriends(user.username());

        assertThat(friends.getUsersList()).isEmpty();
    }

    @RetryingTest(onExceptions = {NullPointerException.class, StatusRuntimeException.class})
    @AllureId("2003")
    @DisplayName("getAllFriends returns a friend with first/last name from nested @CreateFriend")
    @CreateUser(friends = @CreateFriend(firstname = "Joe", lastname = "Mate"))
    void getAllFriendsReturnsNamedFriend(UserJson user) {
        // username stays random (unique-constrained, no cleanup), but first/last
        // name are set by the nested @CreateFriend — the new identity capability.
        String expectedUsername = user.friends().getFirst().username();

        UserArray friends = userdataGrpcClient.getAllFriends(user.username());

        assertThat(friends.getUsersList()).hasSize(1);
        User friend = friends.getUsers(0);
        assertThat(friend.getUsername()).isEqualTo(expectedUsername);
        assertThat(friend.getFirstname()).isEqualTo("Joe");
        assertThat(friend.getSurname()).isEqualTo("Mate");
    }
}
