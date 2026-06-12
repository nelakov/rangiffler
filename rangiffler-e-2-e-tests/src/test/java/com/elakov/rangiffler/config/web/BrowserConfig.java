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

    // Selenide remote (Selenoid/Grid) WebDriver URL, e.g. http://localhost:4444/wd/hub.
    // Empty -> local browser. Overridable via -Dbrowser.remote=... on the command line.
    @Key("browser.remote")
    @DefaultValue("")
    String browserRemote();

    // Base URL Selenoid serves session videos from, e.g. http://localhost:4444/video/.
    // The file is <sessionId>.mp4. Empty -> no video attachment.
    @Key("video.storage")
    @DefaultValue("")
    String videoStorage();
}
