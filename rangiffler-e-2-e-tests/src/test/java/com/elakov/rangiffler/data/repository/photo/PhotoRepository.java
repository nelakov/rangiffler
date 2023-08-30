package com.elakov.rangiffler.data.repository.photo;

import com.elakov.rangiffler.data.entity.photo.PhotoEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public interface PhotoRepository {

    @Nonnull
    PhotoEntity findById(UUID id);

    @Nonnull
    PhotoEntity findByUsername(String username);
    @Nonnull
    List<PhotoEntity> findAllByUsername(String username);

}
