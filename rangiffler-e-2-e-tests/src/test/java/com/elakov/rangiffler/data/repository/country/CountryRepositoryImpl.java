package com.elakov.rangiffler.data.repository.country;

import com.elakov.rangiffler.data.entity.country.CountryEntity;
import com.elakov.rangiffler.data.jpa.EmfProvider;
import com.elakov.rangiffler.data.jpa.JpaTransactionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.elakov.rangiffler.data.ServiceDataBase.COUNTRY_SERVICE;

public class CountryRepositoryImpl extends JpaTransactionManager implements CountryRepository {
    public CountryRepositoryImpl() {
        super(EmfProvider.INSTANCE
                .getEmf(COUNTRY_SERVICE)
                .createEntityManager()
        );
    }

    @Override
    public List<CountryEntity> findAll() {
        return em.createQuery("select c from CountryEntity c", CountryEntity.class)
                .getResultList();
    }

    @NotNull
    @Override
    public CountryEntity findByName(@NotNull String name) {
        return em.createQuery("select c from CountryEntity c where name=:name", CountryEntity.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Nullable
    @Override
    public CountryEntity findByCode(@NotNull String code) {
        return em.createQuery("select c from CountryEntity c where code=:code", CountryEntity.class)
                .setParameter("code", code)
                .getSingleResult();
    }
}
