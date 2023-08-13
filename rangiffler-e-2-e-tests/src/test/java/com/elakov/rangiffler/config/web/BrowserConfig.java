package com.elakov.rangiffler.config.web;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config/com/elakov/rangiffler/services/${env}_web.properties")
interface BrowserConfig extends Config {

}
