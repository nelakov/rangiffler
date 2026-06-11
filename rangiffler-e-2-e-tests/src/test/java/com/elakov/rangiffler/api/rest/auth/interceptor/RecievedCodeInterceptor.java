package com.elakov.rangiffler.api.rest.auth.interceptor;

import com.elakov.rangiffler.api.rest.auth.context.SessionContext;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RecievedCodeInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        String location = response.header("Location");
        if (location != null && location.contains("code=")) {
            SessionContext.getInstance().setCode(location.substring(location.indexOf("code=") + 5));
            // The authorization code is captured from this 302's Location. Rewrite it to
            // a 200 so OkHttp does not follow the redirect to the frontend (:3001), which
            // need not be running for API tests. The token is fetched via a separate
            // /token call using the captured code.
            return response.newBuilder()
                    .code(200)
                    .message("OK (auth redirect short-circuited)")
                    .build();
        }
        return response;
    }
}
