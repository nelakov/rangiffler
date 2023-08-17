package com.elakov.rangiffler.data.repository.authDAO;

import com.elakov.rangiffler.data.entity.AuthUserEntity;
import com.elakov.rangiffler.data.entity.UserEntity;
import com.elakov.rangiffler.data.jpa.EmfProvider;
import com.elakov.rangiffler.data.jpa.JpaTransactionManager;

import static com.elakov.rangiffler.data.ServiceDataBase.AUTH_SERVICE;

public class AuthRepositoryImpl extends JpaTransactionManager implements AuthRepository {

    public AuthRepositoryImpl() {
        super(EmfProvider.INSTANCE
                .getEmf(AUTH_SERVICE)
                .createEntityManager()
        );
    }

    @Override
    public int createUser(AuthUserEntity authUserEntity) {
        authUserEntity.setPassword(encoder.encode(authUserEntity.getPassword()));
        persist(authUserEntity);
        return 0;
    }

    @Override
    public UserEntity getUser(AuthUserEntity authUserEntity) {
        return em.createQuery("select u from UserEntity u where id=:id", UserEntity.class)
                .setParameter("id", authUserEntity.getId())
                .getSingleResult();
    }

    @Override
    public String findUserById(String username) {
        return em.createQuery("select u from UserEntity u where username=:username", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult()
                .getId()
                .toString();
    }

    @Override
    public int removeUser(AuthUserEntity authUserEntity) {
        remove(authUserEntity);
        return 0;
    }


}
