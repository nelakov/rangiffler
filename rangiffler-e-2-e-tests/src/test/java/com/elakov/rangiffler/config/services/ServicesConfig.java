package com.elakov.rangiffler.config.services;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:config/${env}/services_${env}.properties",
        "classpath:config/local/services_local.properties",
        "classpath:config/docker/services_docker.properties"})
public interface ServicesConfig extends Config {

    @Key("client.base.url")
    String clientBaseUrl();

    @Key("auth.base.url")
    String authBaseUrl();

    @Key("country.grpc.service.host")
    String countryGrpcServiceHost();

    @Key("country.grpc.service.port")
    Integer countryGrpcServicePort();

    @Key("photo.grpc.service.host")
    String photoGrpcServiceHost();

    @Key("photo.grpc.service.port")
    Integer photoGrpcServicePort();

    @Key("userdata.base.url")
    String userdataBaseUrl();

    @Key("userdata.grpc.service.host")
    String userdataGrpcServiceHost();

    @Key("userdata.grpc.service.port")
    Integer userdataGrpcServicePort();

}
