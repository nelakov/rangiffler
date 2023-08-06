package com.elakov.rangiffler.data.repository;

import com.elakov.rangiffler.data.UserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Nonnull
    UserEntity findByUsername(@Nonnull String username);
}
