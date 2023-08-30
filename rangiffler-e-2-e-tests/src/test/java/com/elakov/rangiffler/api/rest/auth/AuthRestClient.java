package com.elakov.rangiffler.api.rest.auth;

import com.elakov.rangiffler.api.rest.BaseRestClient;
import com.elakov.rangiffler.api.rest.auth.context.CookieContext;
import com.elakov.rangiffler.api.rest.auth.context.SessionContext;
import com.elakov.rangiffler.api.rest.auth.interceptor.AddCookiesInterceptor;
import com.elakov.rangiffler.api.rest.auth.interceptor.RecievedCodeInterceptor;
import com.elakov.rangiffler.api.rest.auth.interceptor.RecievedCookiesInterceptor;
import org.junit.jupiter.api.Assertions;
import retrofit2.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.elakov.rangiffler.config.services.ServicesProperties.AUTH_BASE_URL;
import static com.elakov.rangiffler.config.services.ServicesProperties.CLIENT_BASE_URL;

//TODO: Настроить логирование для Retrofit
public class AuthRestClient extends BaseRestClient implements AuthClient {
    public AuthRestClient() {
        super(
                AUTH_BASE_URL,
                true,
                new RecievedCookiesInterceptor(),
                new AddCookiesInterceptor(),
                new RecievedCodeInterceptor()
        );
    }

    private final AuthService authService = retrofit.create(AuthService.class);

    @Override
    public void authorizePreRequest() {
        try {
            authService.authorize(
                    "code",
                    "client",
                    "openid",
                    CLIENT_BASE_URL + "/authorized",
                    SessionContext.getInstance().getCodeChallenge(),
                    "S256"
            ).execute();
        } catch (IOException e) {
           Assertions.fail("Can`t execute api call to rangiffler-auth: " + e.getMessage());
        }
    }

    @Override
    public void login(String username, String password) {
        final CookieContext cookieContext = CookieContext.getInstance();

        try {
            authService.login(
                    cookieContext.getFormattedCookie("JSESSIONID"),
                    cookieContext.getFormattedCookie("XSRF-TOKEN"),
                    cookieContext.getCookie("XSRF-TOKEN"),
                    username,
                    password
            ).execute();
        } catch (IOException e) {
            Assertions.fail("Can`t execute api call to rangiffler-auth: " + e.getMessage());
        }
    }

    @Override
    public String getToken() {
        final SessionContext sessionContext = SessionContext.getInstance();

        try {
            final String token = authService.token(
                    "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8)),
                    "client",
                    CLIENT_BASE_URL + "/authorized",
                    "authorization_code",
                    sessionContext.getCode(),
                    sessionContext.getCodeVerifier()
            ).execute().body().get("id_token").asText();
            sessionContext.setToken(token);
            return token;
        } catch (IOException e) {
            Assertions.fail("Can`t execute api call to rangiffler-auth: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void register(String username, String password) {
        CookieContext cookieContext = CookieContext.getInstance();
        try {
            authService.requestRegisterForm().execute();
            Response<Void> response = authService.register(
                            cookieContext.getFormattedCookie("XSRF-TOKEN"),
                            cookieContext.getCookie("XSRF-TOKEN"),
                            username,
                            password,
                            password)
                    .execute();
//            Assertions.assertEquals(201, response.code());
        } catch (IOException e) {
            Assertions.fail("Can`t execute api call to rangiffler-auth: " + e.getMessage());
        }
    }
}
