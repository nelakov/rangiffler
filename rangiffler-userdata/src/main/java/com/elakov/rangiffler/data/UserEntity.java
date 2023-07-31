package com.elakov.rangiffler.data;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.*;
import java.util.stream.Stream;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String firstname;

    @Column(nullable = true)
    private String surname;

    @Column(name = "avatar", columnDefinition = "LONGBLOB")
    private byte[] avatar;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendsEntity> friends = new ArrayList<>();

    @OneToMany(mappedBy = "friend", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendsEntity> invites = new ArrayList<>();

    public void addFriends(boolean pending, UserEntity... friends) {
        List<FriendsEntity> friendsEntities = Stream.of(friends)
                .map(f -> {
                    FriendsEntity fe = new FriendsEntity();
                    fe.setUser(this);
                    fe.setFriend(f);
                    fe.setPending(pending);
                    return fe;
                }).toList();

        this.friends.addAll(friendsEntities);
    }

    public void removeFriends(UserEntity... friends) {
        for (UserEntity friend : friends) {
            getFriends().removeIf(f -> f.getFriend().getId().equals(friend.getId()));
        }
    }

    public void removeInvites(UserEntity... invitations) {
        for (UserEntity invite : invitations) {
            getInvites().removeIf(i -> i.getUser().getId().equals(invite.getId()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}