package com.elakov.rangiffler.config.hibernate;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:config/${env}/hibernate_${env}.properties",
        "classpath:config/local/hibernate_local.properties",
        "classpath:config/docker/hibernate_docker.properties"})
public interface HibernateConfig extends Config {

    @Key("hibernate.username")
    @DefaultValue("root")
    String username();

    @Key("hibernate.password")
    @DefaultValue("secret")
    String password();

    @Key("hibernate.dialect")
    @DefaultValue("org.hibernate.dialect.MySQLDialect")
    String dialect();

    @Key("hibernate.driver_class")
    // p6spy wraps the real MySQL driver so every JDBC statement is logged + attached to Allure
    @DefaultValue("com.p6spy.engine.spy.P6SpyDriver")
    String driverClass();

    @Key("auth.hibernate.url")
    String authUrl();

    @Key("country.hibernate.url")
    String countryUrl();

    @Key("userdata.hibernate.url")
    String userdataUrl();

    @Key("photo.hibernate.url")
    String photoUrl();

}
