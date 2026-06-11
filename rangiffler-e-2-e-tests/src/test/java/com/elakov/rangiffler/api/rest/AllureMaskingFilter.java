package com.elakov.rangiffler.api.rest;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Attaches each request/response to the Allure report with secrets masked, so
 * the OAuth password, Basic/Bearer credentials, cookies and the issued token
 * never end up in the report. Lightweight replacement for the built-in
 * AllureRestAssured filter — public rest-assured API only, no proxy machinery.
 */
public class AllureMaskingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AllureMaskingFilter.class);
    private static final String MASK = "***";

    private static final Set<String> SECRET_HEADERS = Set.of("authorization", "cookie", "set-cookie");
    // form: password=.., code_verifier=.. ; json: "id_token":"..", "password":".."
    private static final Pattern FORM_SECRET =
            Pattern.compile("(password|passwordSubmit|id_token|access_token|code_verifier)=[^&,}\\s]*");
    private static final Pattern JSON_SECRET =
            Pattern.compile("(\"(?:password|passwordSubmit|id_token|access_token|code_verifier)\"\\s*:\\s*\")[^\"]*\"");

    @Override
    public Response filter(FilterableRequestSpecification req,
                           FilterableResponseSpecification resp,
                           FilterContext ctx) {
        Allure.addAttachment("Request", "text/plain", renderRequest(req), ".txt");
        Response response = ctx.next(req, resp);
        Allure.addAttachment("Response", "text/plain", renderResponse(response), ".txt");
        log.debug("{} {} -> {}", req.getMethod(), req.getURI(), response.statusCode());
        return response;
    }

    private String renderRequest(FilterableRequestSpecification req) {
        StringBuilder sb = new StringBuilder()
                .append(req.getMethod()).append(' ').append(req.getURI()).append("\n\n");
        req.getHeaders().forEach(h -> sb.append(h.getName()).append(": ")
                .append(SECRET_HEADERS.contains(h.getName().toLowerCase()) ? MASK : h.getValue())
                .append('\n'));
        if (!req.getFormParams().isEmpty()) {
            sb.append('\n').append(mask(req.getFormParams().toString()));
        } else if (req.getBody() != null) {
            sb.append('\n').append(mask(req.getBody().toString()));
        }
        return sb.toString();
    }

    private String renderResponse(Response response) {
        return response.statusLine() + "\n\n" + mask(response.getBody().asString());
    }

    private static String mask(String text) {
        text = JSON_SECRET.matcher(text).replaceAll("$1" + MASK + "\"");
        text = FORM_SECRET.matcher(text).replaceAll("$1=" + MASK);
        return text;
    }
}
