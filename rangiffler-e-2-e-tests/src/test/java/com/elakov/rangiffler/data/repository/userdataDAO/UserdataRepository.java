package com.elakov.rangiffler.data.repository.userdataDAO;

import com.elakov.rangiffler.data.entity.UserEntity;

import javax.annotation.Nonnull;

public interface UserdataRepository {

    @Nonnull
    UserEntity findByUsername(@Nonnull String username);

}
