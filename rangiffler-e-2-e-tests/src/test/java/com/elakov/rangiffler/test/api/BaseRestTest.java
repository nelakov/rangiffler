package com.elakov.rangiffler.test.api;

import com.elakov.rangiffler.api.rest.auth.AuthClient;
import com.elakov.rangiffler.api.rest.auth.AuthRestClient;
import com.elakov.rangiffler.api.rest.auth.context.CookieContext;
import com.elakov.rangiffler.api.rest.auth.context.SessionContext;
import com.elakov.rangiffler.api.rest.auth.util.OauthUtils;
import com.elakov.rangiffler.api.rest.gateway.GatewayApiClient;
import com.elakov.rangiffler.data.repository.country.CountryRepository;
import com.elakov.rangiffler.data.repository.country.CountryRepositoryImpl;
import com.elakov.rangiffler.data.repository.userdata.UserdataRepository;
import com.elakov.rangiffler.data.repository.userdata.UserdataRepositoryImpl;
import com.elakov.rangiffler.jupiter.annotation.meta.RestTest;
import org.junit.jupiter.api.AfterEach;

@RestTest
public class BaseRestTest {

    protected final AuthClient authClient = new AuthRestClient();
    protected final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    protected final CountryRepository countryRepository = new CountryRepositoryImpl();
    protected final UserdataRepository userdataRepository = new UserdataRepositoryImpl();

    /**
     * Browserless OAuth2 PKCE login — runs the same authorize/login/token flow as
     * {@code ApiLoginCallback} but without opening Selenide, so pure REST tests
     * don't need a running frontend. Returns the id_token (JWT).
     */
    protected String login(String username, String password) {
        try {
            SessionContext session = SessionContext.getInstance();
            String codeVerifier = OauthUtils.generateCodeVerifier();
            session.setCodeChallenge(OauthUtils.generateCodeChallenge(codeVerifier));
            session.setCodeVerifier(codeVerifier);

            authClient.authorizePreRequest();
            authClient.login(username, password);
            return authClient.getToken();
        } catch (Throwable e) {
            throw new RuntimeException("Browserless login failed for " + username, e);
        }
    }

    @AfterEach
    void releaseAuthContext() {
        SessionContext.getInstance().release();
        CookieContext.getInstance().release();
    }
}
