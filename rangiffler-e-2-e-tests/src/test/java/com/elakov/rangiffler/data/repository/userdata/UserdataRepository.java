package com.elakov.rangiffler.data.repository.userdata;

import com.elakov.rangiffler.data.entity.userdata.UserEntity;

import javax.annotation.Nonnull;

public interface UserdataRepository {

    @Nonnull
    UserEntity findByUsername(@Nonnull String username);

}
