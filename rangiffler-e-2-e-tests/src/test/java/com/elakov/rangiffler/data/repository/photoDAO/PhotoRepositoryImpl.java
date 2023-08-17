package com.elakov.rangiffler.data.repository.photoDAO;

import com.elakov.rangiffler.data.ServiceDataBase;
import com.elakov.rangiffler.data.entity.PhotoEntity;
import com.elakov.rangiffler.data.jpa.EmfProvider;
import com.elakov.rangiffler.data.jpa.JpaTransactionManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class PhotoRepositoryImpl extends JpaTransactionManager implements PhotoRepository {

    public PhotoRepositoryImpl() {
        super(EmfProvider.INSTANCE
                .getEmf(ServiceDataBase.PHOTO_SERVICE)
                .createEntityManager()
        );
    }

    @NotNull
    @Override
    public PhotoEntity findById(UUID id) {
        return em.createQuery("SELECT p FROM PhotoEntity p WHERE p.id=:id", PhotoEntity.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @Override
    public List<PhotoEntity> findAllByUsername(String username) {
        return  em.createQuery("SELECT p FROM PhotoEntity p WHERE p.username=:username", PhotoEntity.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .toList();
    }

}
