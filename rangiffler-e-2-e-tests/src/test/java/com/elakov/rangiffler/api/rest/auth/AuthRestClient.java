package com.elakov.rangiffler.api.rest.auth;

import com.elakov.rangiffler.api.rest.BaseRestClient;
import com.elakov.rangiffler.api.rest.auth.context.CookieContext;
import com.elakov.rangiffler.api.rest.auth.context.SessionContext;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.elakov.rangiffler.config.services.ServicesProperties.AUTH_BASE_URL;
import static com.elakov.rangiffler.config.services.ServicesProperties.CLIENT_BASE_URL;

/**
 * OAuth2 PKCE client for rangiffler-auth, on rest-assured.
 *
 * The JSESSIONID/XSRF-TOKEN cookies are carried across the authorize -> login ->
 * token chain via {@link CookieContext} (a thread-local store, captured from and
 * replayed onto each request — the same role the old OkHttp interceptors played).
 * Redirects are NOT auto-followed: the chain is walked manually so the final hop
 * to the (absent) frontend is never executed; the authorization code is scraped
 * from its Location instead.
 */
public class AuthRestClient extends BaseRestClient implements AuthClient {

    private static final String JSESSIONID = "JSESSIONID";
    private static final String XSRF = "XSRF-TOKEN";
    private static final int MAX_REDIRECTS = 6;

    public AuthRestClient() {
        super(AUTH_BASE_URL);
    }

    @Override
    public void authorizePreRequest() {
        Response authorize = withCookies(spec().redirects().follow(false))
                .queryParam("response_type", "code")
                .queryParam("client_id", "client")
                .queryParam("scope", "openid")
                .queryParam("redirect_uri", CLIENT_BASE_URL + "/authorized")
                .queryParam("code_challenge", SessionContext.getInstance().getCodeChallenge())
                .queryParam("code_challenge_method", "S256")
                .get("/oauth2/authorize");
        capture(authorize);

        // Follow the 302 to the login page so the CSRF cookie is issued.
        if (isRedirect(authorize)) {
            Response loginPage = withCookies(spec().redirects().follow(false)).get(authorize.header("Location"));
            capture(loginPage);
        }
    }

    @Override
    public void login(String username, String password) {
        Response login = withCookies(spec().redirects().follow(false))
                .contentType(ContentType.URLENC)
                .formParam("_csrf", CookieContext.getInstance().getCookie(XSRF))
                .formParam("username", username)
                .formParam("password", password)
                .post("/login");
        capture(login);

        String code = followUntilCode(login);
        SessionContext.getInstance().setCode(code);
    }

    @Override
    public String getToken() {
        String basic = "Basic " + Base64.getEncoder()
                .encodeToString("client:secret".getBytes(StandardCharsets.UTF_8));
        String token = spec()
                .header("Authorization", basic)
                .contentType(ContentType.URLENC)
                .formParam("client_id", "client")
                .formParam("redirect_uri", CLIENT_BASE_URL + "/authorized")
                .formParam("grant_type", "authorization_code")
                .formParam("code", SessionContext.getInstance().getCode())
                .formParam("code_verifier", SessionContext.getInstance().getCodeVerifier())
                .post("/oauth2/token")
                .jsonPath().getString("id_token");
        SessionContext.getInstance().setToken(token);
        return token;
    }

    @Override
    public void register(String username, String password) {
        // Register runs its own short session (own cookies), separate from login.
        Response form = spec().redirects().follow(false).get("/register");
        String xsrf = form.getCookie(XSRF);

        spec().redirects().follow(false)
                .cookie(XSRF, xsrf)
                .contentType(ContentType.URLENC)
                .formParam("_csrf", xsrf)
                .formParam("username", username)
                .formParam("password", password)
                .formParam("passwordSubmit", password)
                .post("/register");
    }

    /** Walks the post-login 302 chain, returning the code from the first Location that carries one. */
    private String followUntilCode(Response response) {
        Response current = response;
        for (int hop = 0; hop < MAX_REDIRECTS && isRedirect(current); hop++) {
            String location = current.header("Location");
            if (location == null) {
                break;
            }
            if (location.contains("code=")) {
                return extractQueryParam(location, "code");
            }
            // Location is already percent-encoded (redirect_uri); disable rest-assured's
            // own encoding so it isn't double-encoded (%3A -> %253A -> auth 400).
            current = withCookies(spec().redirects().follow(false).urlEncodingEnabled(false)).get(location);
            capture(current);
        }
        Assertions.fail("Authorization code not found in the redirect chain after login");
        return null;
    }

    /** Replays the stored JSESSIONID/XSRF-TOKEN onto the request (cf. AddCookiesInterceptor). */
    private RequestSpecification withCookies(RequestSpecification spec) {
        CookieContext cookies = CookieContext.getInstance();
        if (cookies.getCookie(JSESSIONID) != null) {
            spec.cookie(JSESSIONID, cookies.getCookie(JSESSIONID));
        }
        if (cookies.getCookie(XSRF) != null) {
            spec.cookie(XSRF, cookies.getCookie(XSRF));
        }
        return spec;
    }

    /** Stores any JSESSIONID/XSRF-TOKEN set by the response (cf. RecievedCookiesInterceptor). */
    private void capture(Response response) {
        for (Cookie cookie : response.getDetailedCookies().asList()) {
            if (JSESSIONID.equals(cookie.getName()) || XSRF.equals(cookie.getName())) {
                CookieContext.getInstance().setCookie(cookie.getName(), cookie.getValue());
            }
        }
    }

    private static boolean isRedirect(Response response) {
        return response.statusCode() >= 300 && response.statusCode() < 400;
    }

    private static String extractQueryParam(String url, String name) {
        String marker = name + "=";
        int start = url.indexOf(marker);
        if (start < 0) {
            return null;
        }
        start += marker.length();
        int end = url.indexOf('&', start);
        String raw = end < 0 ? url.substring(start) : url.substring(start, end);
        return URLDecoder.decode(raw, StandardCharsets.UTF_8);
    }
}
