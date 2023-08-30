package com.elakov.rangiffler.data.entity.userdata;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@RequiredArgsConstructor
@IdClass(FriendsId.class)
@Entity
@Table(name = "friends")
public class FriendsEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id", nullable = false)
    private UserEntity friend;

    @Column(name = "pending", nullable = false)
    private boolean pending;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FriendsEntity that = (FriendsEntity) o;
        return user != null && Objects.equals(user, that.user)
                && friend != null && Objects.equals(friend, that.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }

}