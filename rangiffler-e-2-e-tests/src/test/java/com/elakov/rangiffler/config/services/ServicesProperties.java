package com.elakov.rangiffler.config.services;

import org.aeonbits.owner.ConfigCache;

public final class ServicesProperties {

    // Singleton
    private static final ServicesConfig SERVICES_CONFIG = ConfigCache.getOrCreate(ServicesConfig.class, System.getProperties());



}
