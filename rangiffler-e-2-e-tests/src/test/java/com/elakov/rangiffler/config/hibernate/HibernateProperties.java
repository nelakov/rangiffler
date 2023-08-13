package com.elakov.rangiffler.config.hibernate;

import org.aeonbits.owner.ConfigCache;

public final class HibernateProperties {

    // Singleton
    private static final HibernateConfig HIBERNATE_CONFIG = ConfigCache.getOrCreate(HibernateConfig.class, System.getProperties());
    public static final String HIBERNATE_USERNAME = HIBERNATE_CONFIG.username();
    public static final String HIBERNATE_PASSWORD = HIBERNATE_CONFIG.password();
    public static final String HIBERNATE_DIALECT = HIBERNATE_CONFIG.dialect();
    public static final String HIBERNATE_DRIVER_CLASS = HIBERNATE_CONFIG.driverClass();
    public static final String AUTH_SERVICE_DB = HIBERNATE_CONFIG.authUrl();
    public static final String COUNTRY_SERVICE_DB = HIBERNATE_CONFIG.countryUrl();
    public static final String PHOTO_SERVICE_DB = HIBERNATE_CONFIG.photoUrl();
    public static final String USERDATA_SERVICE_DB = HIBERNATE_CONFIG.userdataUrl();
}
