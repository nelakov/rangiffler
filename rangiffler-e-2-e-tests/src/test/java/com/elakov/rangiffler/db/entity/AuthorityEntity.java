package com.elakov.rangiffler.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Data
@ToString(exclude = "authorities")
@Entity
@Table(name = "authorities")
public class AuthorityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityEntity that = (AuthorityEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(authority, that.authority) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority, user);
    }
}