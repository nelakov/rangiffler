package com.elakov.rangiffler.data.repository.userdataDAO;

import com.elakov.rangiffler.data.entity.UserEntity;
import com.elakov.rangiffler.data.jpa.EmfProvider;
import com.elakov.rangiffler.data.jpa.JpaTransactionManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.elakov.rangiffler.data.ServiceDataBase.USERDATA_SERVICE;

public class UserdataRepositoryImpl extends JpaTransactionManager implements UserdataRepository {

    public UserdataRepositoryImpl() {
        super(EmfProvider.INSTANCE
                .getEmf(USERDATA_SERVICE)
                .createEntityManager()
        );
    }

    @NotNull
    @Override
    public UserEntity findByUsername(@NotNull String username) {
        return Objects.requireNonNull(em.createQuery("SELECT u FROM UserEntity u WHERE u.username=:username", UserEntity.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst().orElse(null));
    };

}
