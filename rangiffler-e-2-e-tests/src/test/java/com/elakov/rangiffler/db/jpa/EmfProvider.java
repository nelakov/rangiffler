package com.elakov.rangiffler.db.jpa;

import com.elakov.rangiffler.db.ServiceDataBase;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.elakov.rangiffler.config.hibernate.HibernateProperties.*;
import static jakarta.persistence.Persistence.createEntityManagerFactory;

public enum EmfProvider {
    INSTANCE;

    private final Map<ServiceDataBase, EntityManagerFactory> emfStore = new ConcurrentHashMap<>();

    public EntityManagerFactory getEmf(ServiceDataBase serviceDataBase) {
        return emfStore.computeIfAbsent(serviceDataBase, db -> {
            Map<String, Object> properties = new HashMap<>();
            properties.put("hibernate.dialect", HIBERNATE_DIALECT);
            properties.put("hibernate.connection.driver_class", HIBERNATE_DRIVER_CLASS);
            properties.put("hibernate.connection.username", HIBERNATE_USERNAME);
            properties.put("hibernate.connection.password", HIBERNATE_PASSWORD);
            properties.put("hibernate.connection.url", serviceDataBase.toString());

            return new ThreadLocalEmf(
                    createEntityManagerFactory("niffler-persistence-unit-name", properties));
        });
    }
}
