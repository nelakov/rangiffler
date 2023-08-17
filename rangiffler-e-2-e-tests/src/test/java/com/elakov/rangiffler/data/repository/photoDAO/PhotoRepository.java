package com.elakov.rangiffler.data.repository.photoDAO;

import com.elakov.rangiffler.data.entity.PhotoEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface PhotoRepository {

    @Nonnull
    PhotoEntity findById(UUID id);

    @Nonnull
    List<PhotoEntity> findAllByUsername(String username);

}
