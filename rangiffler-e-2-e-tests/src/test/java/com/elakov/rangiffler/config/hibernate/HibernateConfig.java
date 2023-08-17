package com.elakov.rangiffler.config.hibernate;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;

import static org.aeonbits.owner.Config.LoadType.MERGE;

@LoadPolicy(MERGE)
@Sources({"classpath:config/com/elakov/rangiffler/hibernate/${env}_db.properties",
        "classpath:config/com/elakov/rangiffler/hibernate/local_db.properties"})
interface HibernateConfig extends Config {

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
