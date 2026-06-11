package com.elakov.rangiffler.service;

import com.elakov.rangiffler.data.FriendsEntity;
import com.elakov.rangiffler.data.UserEntity;
import com.elakov.rangiffler.data.repository.UserRepository;
import com.elakov.rangiffler.exception.NotFoundException;
import com.elakov.rangiffler.model.FriendJson;
import com.elakov.rangiffler.model.FriendStatus;
import com.elakov.rangiffler.model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDataServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDataService userDataService;

    private static UserEntity user(String username) {
        UserEntity e = new UserEntity();
        e.setId(UUID.randomUUID());
        e.setUsername(username);
        return e;
    }

    private static FriendsEntity invite(UserEntity from, boolean pending) {
        FriendsEntity fe = new FriendsEntity();
        fe.setUser(from);
        fe.setPending(pending);
        return fe;
    }

    // --- update ---

    @Test
    @DisplayName("update sets profile fields and persists")
    void updateExisting() {
        UserEntity entity = user("bob");
        when(userRepository.findByUsername("bob")).thenReturn(entity);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        UserJson result = userDataService.update(new UserJson(null, "bob", "Bob", "Smith", null, null));

        assertThat(result.firstname()).isEqualTo("Bob");
        assertThat(result.lastName()).isEqualTo("Smith");
        assertThat(entity.getFirstname()).isEqualTo("Bob");
        assertThat(entity.getLastname()).isEqualTo("Smith");
    }

    @Test
    @DisplayName("update throws NotFound when user is absent")
    void updateMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> userDataService.update(new UserJson(null, "ghost", null, null, null, null)))
                .isInstanceOf(NotFoundException.class);
        verify(userRepository, never()).save(any());
    }

    // --- getCurrentUserOrCreateIfAbsent ---

    @Test
    @DisplayName("getCurrentUserOrCreateIfAbsent returns existing without saving")
    void currentUserExisting() {
        when(userRepository.findByUsername("bob")).thenReturn(user("bob"));

        UserJson result = userDataService.getCurrentUserOrCreateIfAbsent("bob");

        assertThat(result.username()).isEqualTo("bob");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("getCurrentUserOrCreateIfAbsent creates and saves when absent")
    void currentUserCreated() {
        when(userRepository.findByUsername("newbie")).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        UserJson result = userDataService.getCurrentUserOrCreateIfAbsent("newbie");

        assertThat(result.username()).isEqualTo("newbie");
        verify(userRepository).save(any(UserEntity.class));
    }

    // --- friends ---

    @Test
    @DisplayName("friends returns accepted friends only")
    void friendsReturnsAccepted() {
        UserEntity bob = user("bob");
        UserEntity alice = user("alice");
        UserEntity carol = user("carol");
        bob.addFriends(false, alice); // accepted
        bob.addFriends(true, carol);  // still pending -> excluded
        when(userRepository.findByUsername("bob")).thenReturn(bob);

        List<UserJson> friends = userDataService.friends("bob");

        assertThat(friends).extracting(UserJson::username).containsExactly("alice");
        assertThat(friends).extracting(UserJson::friendStatus).containsOnly(FriendStatus.FRIEND);
    }

    @Test
    @DisplayName("friends throws NotFound when user absent")
    void friendsMissing() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);
        assertThatThrownBy(() -> userDataService.friends("ghost")).isInstanceOf(NotFoundException.class);
    }

    // --- invitations ---

    @Test
    @DisplayName("invitations returns pending received invites")
    void invitationsReturnsPending() {
        UserEntity bob = user("bob");
        UserEntity alice = user("alice");
        bob.setInvites(List.of(invite(alice, true)));
        when(userRepository.findByUsername("bob")).thenReturn(bob);

        List<UserJson> invites = userDataService.invitations("bob");

        assertThat(invites).extracting(UserJson::username).containsExactly("alice");
        assertThat(invites).extracting(UserJson::friendStatus).containsOnly(FriendStatus.INVITATION_RECEIVED);
    }

    // --- addFriend ---

    @Test
    @DisplayName("addFriend links friend as pending invitation and saves")
    void addFriendHappy() {
        UserEntity bob = user("bob");
        UserEntity alice = user("alice");
        when(userRepository.findByUsername("bob")).thenReturn(bob);
        when(userRepository.findByUsername("alice")).thenReturn(alice);

        UserJson result = userDataService.addFriend("bob", new FriendJson("alice"));

        assertThat(result.username()).isEqualTo("alice");
        assertThat(result.friendStatus()).isEqualTo(FriendStatus.INVITATION_SENT);
        assertThat(bob.getFriends()).hasSize(1);
        verify(userRepository).save(bob);
    }

    @Test
    @DisplayName("addFriend throws NotFound when target friend absent")
    void addFriendMissingFriend() {
        when(userRepository.findByUsername("bob")).thenReturn(user("bob"));
        when(userRepository.findByUsername("ghost")).thenReturn(null);

        assertThatThrownBy(() -> userDataService.addFriend("bob", new FriendJson("ghost")))
                .isInstanceOf(NotFoundException.class);
    }

    // --- acceptInvitation ---

    @Test
    @DisplayName("acceptInvitation flips pending to false and adds friendship")
    void acceptInvitationHappy() {
        UserEntity bob = user("bob");
        UserEntity alice = user("alice");
        FriendsEntity incoming = invite(alice, true);
        bob.setInvites(new java.util.ArrayList<>(List.of(incoming)));
        when(userRepository.findByUsername("bob")).thenReturn(bob);
        when(userRepository.findByUsername("alice")).thenReturn(alice);

        UserJson result = userDataService.acceptInvitation("bob", new FriendJson("alice"));

        assertThat(result.username()).isEqualTo("alice");
        assertThat(result.friendStatus()).isEqualTo(FriendStatus.FRIEND);
        assertThat(incoming.isPending()).isFalse();
        verify(userRepository).save(bob);
    }

    // --- removeFriend ---

    @Test
    @DisplayName("removeFriend unlinks both sides and saves both")
    void removeFriendHappy() {
        UserEntity bob = user("bob");
        UserEntity alice = user("alice");
        when(userRepository.findByUsername("bob")).thenReturn(bob);
        when(userRepository.findByUsername("alice")).thenReturn(alice);

        UserJson result = userDataService.removeFriend("bob", "alice");

        assertThat(result.username()).isEqualTo("alice");
        assertThat(result.friendStatus()).isEqualTo(FriendStatus.NOT_FRIEND);
        verify(userRepository).save(bob);
        verify(userRepository).save(alice);
    }

    @Test
    @DisplayName("removeFriend throws NotFound when current user absent")
    void removeFriendMissingUser() {
        when(userRepository.findByUsername("ghost")).thenReturn(null);
        assertThatThrownBy(() -> userDataService.removeFriend("ghost", "alice"))
                .isInstanceOf(NotFoundException.class);
    }
}
