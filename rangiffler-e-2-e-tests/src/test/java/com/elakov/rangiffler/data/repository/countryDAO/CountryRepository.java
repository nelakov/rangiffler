package com.elakov.rangiffler.data.repository.countryDAO;

import com.elakov.rangiffler.data.entity.CountryEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface CountryRepository {

    List<CountryEntity> findAll();

    @Nonnull
    CountryEntity findByName(@Nonnull String username);

    @Nullable
    CountryEntity findByCode(@Nonnull String code);

}
