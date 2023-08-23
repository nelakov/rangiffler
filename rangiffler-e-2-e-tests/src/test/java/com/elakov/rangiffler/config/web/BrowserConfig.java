package com.elakov.rangiffler.config.web;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:config/${env}/web_${env}.properties",
        "classpath:config/local/web_local.properties",
        "classpath:config/docker/web_docker.properties"})
public interface BrowserConfig extends Config {

    @Key("browser.name")
    String browserName();

    @Key("browser.version")
    String browserVersion();

    @Key("browser.size")
    String browserSize();


}
