package com.elakov.rangiffler.config.hibernate;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config/com/elakov/rangiffler/hibernate/${env}_db.properties")
interface HibernateConfig extends Config {

    @Key("hibernate.username")
    @DefaultValue("root")
    String username();

    @Key("hibernate.password")
    @DefaultValue("secret")
    String password();

    @Key("hibernate.dialect")
    @DefaultValue("org.hibernate.dialect.MySQL8Dialect")
    String dialect();

    @Key("hibernate.driver_class")
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
