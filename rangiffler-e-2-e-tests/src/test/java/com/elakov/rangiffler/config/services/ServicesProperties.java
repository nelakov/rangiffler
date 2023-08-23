package com.elakov.rangiffler.config.services;

import org.aeonbits.owner.ConfigCache;
public final class ServicesProperties {

    // Singleton
    private static final ServicesConfig SERVICES_CONFIG = ConfigCache.getOrCreate(ServicesConfig.class, System.getProperties());

    public static final String CLIENT_BASE_URL = SERVICES_CONFIG.clientBaseUrl();
    public static final String AUTH_BASE_URL = SERVICES_CONFIG.authBaseUrl();
    public static final String COUNTRY_GRPC_HOST = SERVICES_CONFIG.countryGrpcServiceHost();
    public static final int COUNTRY_GRPC_PORT = SERVICES_CONFIG.countryGrpcServicePort();

    public static final String PHOTO_GRPC_HOST = SERVICES_CONFIG.photoGrpcServiceHost();
    public static final int PHOTO_GRPC_PORT = SERVICES_CONFIG.photoGrpcServicePort();

    public static final String USERDATA_GRPC_HOST = SERVICES_CONFIG.userdataGrpcServiceHost();

    public static final int USERDATA_GRPC_PORT = SERVICES_CONFIG.userdataGrpcServicePort();


}
