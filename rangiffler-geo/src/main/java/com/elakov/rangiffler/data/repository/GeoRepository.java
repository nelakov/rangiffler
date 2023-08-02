package com.elakov.rangiffler.data.repository;

import com.elakov.rangiffler.data.GeoEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GeoRepository extends JpaRepository<GeoEntity, UUID> {

    @Nonnull
    List<GeoEntity> findAllByName(@Nonnull String username);

    @Nullable
    GeoEntity findByCode(@Nonnull String code);

}
