package com.elakov.rangiffler.data.repository.auth;

import com.elakov.rangiffler.data.entity.auth.UserAuthEntity;
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
    public int createUser(UserAuthEntity userAuthEntity) {
        userAuthEntity.setPassword(PASSWORD_ENCODER.encode(userAuthEntity.getPassword()));
        persist(userAuthEntity);
        return 0;
    }

    @Override
    public UserAuthEntity getUser(UserAuthEntity userAuthEntity) {
        return em.createQuery("select u from UserAuthEntity u where id=:id", UserAuthEntity.class)
                .setParameter("id", userAuthEntity.getId())
                .getSingleResult();
    }

    @Override
    public UserAuthEntity getUserByUsername(String username) {
        return em.createQuery("select u from UserAuthEntity u where username=:username", UserAuthEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public String findUserById(String username) {
        return em.createQuery("select u from UserAuthEntity u where username=:username", UserAuthEntity.class)
                .setParameter("username", username)
                .getSingleResult()
                .getId()
                .toString();
    }

    @Override
    public int removeUser(UserAuthEntity user) {
        remove(user);
        return 0;
    }
}
