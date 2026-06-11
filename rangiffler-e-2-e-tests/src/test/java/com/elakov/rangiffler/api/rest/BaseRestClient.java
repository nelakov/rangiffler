package com.elakov.rangiffler.api.rest;

import com.elakov.rangiffler.config.services.ServicesConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigCache;

import static io.restassured.RestAssured.given;

/**
 * rest-assured base for the e2e REST clients. {@link #spec()} returns a request
 * spec bound to the service base URI with the Allure filter attached, so every
 * request/response is auto-attached to the report. Subclasses add what they
 * need (cookies, redirect control, bearer token).
 */
public abstract class BaseRestClient {

    protected static final ServicesConfig CFG = ConfigCache.getOrCreate(ServicesConfig.class, System.getProperties());

    protected final String baseUri;

    protected BaseRestClient(String baseUri) {
        this.baseUri = baseUri;
    }

    protected RequestSpecification spec() {
        return given()
                .baseUri(baseUri)
                .filter(new AllureMaskingFilter());
    }

    /**
     * Deserializes the body only on a 2xx response, else returns null — matching
     * the prior Retrofit clients ({@code response.body()} was null on non-2xx).
     * Keeps the transient currentUser/Kafka create race surfacing as the same
     * downstream NPE the retry net already absorbs, rather than a parse error.
     */
    protected <T> T asOrNull(Response response, Class<T> type) {
        return (response.statusCode() >= 200 && response.statusCode() < 300)
                ? response.as(type)
                : null;
    }
}
