package com.elakov.rangiffler.jupiter.callback.creation;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.elakov.rangiffler.api.rest.auth.AuthClient;
import com.elakov.rangiffler.api.rest.auth.AuthRestClient;
import com.elakov.rangiffler.api.rest.auth.context.CookieContext;
import com.elakov.rangiffler.api.rest.auth.context.SessionContext;
import com.elakov.rangiffler.api.rest.auth.util.OauthUtils;
import com.elakov.rangiffler.jupiter.annotation.test.ApiLogin;
import com.elakov.rangiffler.jupiter.callback.service.UserService;
import com.elakov.rangiffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import java.util.Objects;

import static com.elakov.rangiffler.config.services.ServicesProperties.CLIENT_BASE_URL;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class ApiLoginCallback implements BeforeEachCallback, AfterTestExecutionCallback {

    private static final UserService USER_SERVICE = new UserService();
    private static final AuthClient authClient = new AuthRestClient();
    private static final String JSESSIONID = "JSESSIONID";

    @Override
    public void beforeEach(ExtensionContext context) {
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null) {
            String username;
            String password;

            if ("".equals(apiLoginAnnotation.username()) || "".equals(apiLoginAnnotation.password())) {
                if (isEmpty(apiLoginAnnotation.user())) {
                    throw new IllegalStateException();
                } else {
                    UserJson createdUser = USER_SERVICE.createUserViaApi(apiLoginAnnotation.user()[0]);
                    username = createdUser.getUsername();
                    password = createdUser.getPassword();
                    context.getStore(CreateUserCallback.CREATE_USER_NAMESPACE).put(getTestId(context), createdUser);
                }
            } else {
                username = apiLoginAnnotation.username();
                password = apiLoginAnnotation.password();
            }
            try {
                doLogin(username, password);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void doLogin(String username, String password) throws Throwable {
        final SessionContext sessionContext = SessionContext.getInstance();

        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallange = OauthUtils.generateCodeChallenge(codeVerifier);

        sessionContext.setCodeChallenge(codeChallange);
        sessionContext.setCodeVerifier(codeVerifier);

        authClient.authorizePreRequest();
        authClient.login(username, password);
        final String token = authClient.getToken();
        setUpBrowser(token);
    }

    private void setUpBrowser(String token) {
        SessionContext sessionContext = SessionContext.getInstance();
        CookieContext cookieContext = CookieContext.getInstance();
        Selenide.open(CLIENT_BASE_URL);
        Selenide.sessionStorage().setItem("id_token", token);
        Selenide.sessionStorage().setItem("codeChallenge", sessionContext.getCodeChallenge());
        Selenide.sessionStorage().setItem("codeVerifier", sessionContext.getCodeVerifier());
        Cookie jssesionIdCookie = new Cookie(JSESSIONID, cookieContext.getCookie(JSESSIONID));
        WebDriverRunner.getWebDriver().manage().addCookie(jssesionIdCookie);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        SessionContext.getInstance().release();
        CookieContext.getInstance().release();
    }

    private String getTestId(ExtensionContext context) {
        return Objects
                .requireNonNull(context.getRequiredTestMethod().getAnnotation(AllureId.class))
                .value();
    }
}