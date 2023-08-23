package com.elakov.rangiffler.api.rest;

import com.elakov.rangiffler.config.services.ServicesConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import org.aeonbits.owner.ConfigCache;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Slf4j
public abstract class BaseRestClient {

    protected static final ServicesConfig CFG = ConfigCache.getOrCreate(ServicesConfig.class, System.getProperties());
    protected final String serviceBaseUrl;
    protected final OkHttpClient httpClient;
    protected final Retrofit retrofit;
    public BaseRestClient(String serviceBaseUrl) {
        this(serviceBaseUrl, false, null);
    }
    public BaseRestClient(String serviceBaseUrl, boolean followRedirect) {
        this(serviceBaseUrl, followRedirect, null);
    }

    public BaseRestClient(String serviceBaseUrl, boolean followRedirect, Interceptor... interceptors) {
        this.serviceBaseUrl = serviceBaseUrl;
        Builder builder = new Builder()
                .followRedirects(followRedirect);

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY));
        this.httpClient = builder.build();

        this.retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(serviceBaseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

}
