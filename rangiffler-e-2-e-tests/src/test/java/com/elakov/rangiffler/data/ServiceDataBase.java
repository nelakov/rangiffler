package com.elakov.rangiffler.data;

import com.elakov.rangiffler.config.hibernate.HibernateProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ServiceDataBase {

    AUTH_SERVICE(HibernateProperties.AUTH_SERVICE_DB),
    COUNTRY_SERVICE(HibernateProperties.COUNTRY_SERVICE_DB),
    PHOTO_SERVICE(HibernateProperties.PHOTO_SERVICE_DB),
    USERDATA_SERVICE(HibernateProperties.USERDATA_SERVICE_DB);

    private final String url;

    @Override
    public String toString() {
        return url;
    }

}
