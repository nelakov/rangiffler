package com.elakov.rangiffler.data.jpa;

import com.elakov.rangiffler.data.ServiceDataBase;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.elakov.rangiffler.config.hibernate.HibernateProperties.*;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;
import static jakarta.persistence.Persistence.createEntityManagerFactory;

@Slf4j
public enum EmfProvider {
    INSTANCE;
    private final Map<ServiceDataBase, EntityManagerFactory> emfStore = new ConcurrentHashMap<>();

    public EntityManagerFactory getEmf(ServiceDataBase serviceDataBase) {
        return step("Set up database connection", () -> emfStore.computeIfAbsent(serviceDataBase, db -> {
            log.debug("creating emf for unit {}", serviceDataBase.name());
            Map<String, Object> properties = new HashMap<>();
            log.debug("using dbConfig '{}' to create emf", properties);
            properties.put("hibernate.dialect", HIBERNATE_DIALECT);
            properties.put("hibernate.connection.driver_class", HIBERNATE_DRIVER_CLASS);
            properties.put("hibernate.connection.username", HIBERNATE_USERNAME);
            properties.put("hibernate.connection.password", HIBERNATE_PASSWORD);
            properties.put("hibernate.connection.url", serviceDataBase.toString());

            return new ThreadLocalEmf(
                    createEntityManagerFactory("rangiffler-persistence-unit-name", properties));
        }));
    }

    public Collection<EntityManagerFactory> closeEmf() {
        return emfStore.values();
    }
}
