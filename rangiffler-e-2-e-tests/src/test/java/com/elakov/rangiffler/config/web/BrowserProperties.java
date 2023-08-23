package com.elakov.rangiffler.config.web;

import org.aeonbits.owner.ConfigCache;

public final class BrowserProperties {

    public static BrowserConfig CFG = ConfigCache.getOrCreate(BrowserConfig.class, System.getProperties());
    public static final String BROWSER_NAME = CFG.browserName();
    public static final String BROWSER_VERSION = CFG.browserVersion();
    public static final String BROWSER_SIZE = CFG.browserSize();

}
