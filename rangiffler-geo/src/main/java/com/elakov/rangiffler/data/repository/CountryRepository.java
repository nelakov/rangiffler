package com.elakov.rangiffler.data.repository;

import com.elakov.rangiffler.data.CountryEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    @Nonnull
    List<CountryEntity> findAllByName(@Nonnull String username);

    @Nullable
    CountryEntity findByCode(@Nonnull String code);

}
