package com.elakov.rangiffler.data.repository;

import com.elakov.rangiffler.data.PhotoEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID>{

    @Nonnull
    List<PhotoEntity> findAllByUsername(String username);

    @Nonnull
    List<PhotoEntity> findAllByUsernameIn(Collection<String> usernames);
}
