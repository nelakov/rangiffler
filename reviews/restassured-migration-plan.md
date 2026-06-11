# Retrofit → rest-assured migration (branch feature/restassured-migration)

Decision: keep `*RestClient` public signatures identical; logging infra in same migration.

## Deps (DONE)
- catalog: rest-assured=6.0.0, json-unit=5.1.2; libs rest-assured/allure-rest-assured/json-unit-assertj; bundle `rest-assured`.
- e2e build.gradle: added `libs.bundles.rest.assured` alongside retrofit (retrofit removed at end).

## Keep
- `AuthClient` interface, `SessionContext` (ThreadLocal: verifier/challenge/code/token), `OauthUtils` (PKCE gen), models (UserJson/FriendJson/CountryJson — Jackson).
- Public APIs: AuthRestClient(authorizePreRequest/login/getToken/register), UserdataRestClient(currentUser/addFriend/acceptInvitation/updateUserInfo/allUsers), GatewayApiClient(allCountries/currentUser/allUsers/currentUserStatus).

## Delete
- Retrofit interfaces: AuthService, UserdataService, GatewayApi.
- Interceptors: AddCookiesInterceptor, RecievedCookiesInterceptor, RecievedCodeInterceptor.
- CookieContext (replaced by rest-assured CookieFilter per flow).

## OAuth re-wire (the risk) — rest-assured
Flow order (BaseRestTest.login + ApiLoginCallback.doLogin): generate verifier+challenge → authorizePreRequest → login → getToken. register is SEPARATE (UserService.createUserViaApi, own authClient instance).
CookieFilter held per-flow in a ThreadLocal holder (created in authorizePreRequest, released in afterEach alongside SessionContext). register uses its own local CookieFilter.

- authorizePreRequest: GET /oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=CLIENT/authorized&code_challenge=..&code_challenge_method=S256, follow(false) → 302 Location=/login; manually GET /login (same CookieFilter) → captures XSRF-TOKEN cookie (store value for the _csrf form field).
- login: POST /login (form: _csrf, username, password) follow(false) → 302 Location=/oauth2/authorize(saved); GET that follow(false) → 302 Location=CLIENT/authorized?code=XXX; extract `code` from query, SessionContext.setCode. Do NOT GET the frontend.
- getToken: POST /oauth2/token (Basic client:secret, form: client_id, redirect_uri, grant_type=authorization_code, code, code_verifier) → jsonPath id_token. SessionContext.setToken.
- register: GET /register (fresh CookieFilter) → capture XSRF cookie; POST /register (form: _csrf, username, password, passwordSubmit).

Cookie names: JSESSIONID, XSRF-TOKEN. Token endpoint creds: client:secret (Basic).

## Phase plan
- 2a: clients on rest-assured + built-in `io.qameta.allure.restassured.AllureRestAssured` filter (free req/resp→Allure). Delete Retrofit interfaces/interceptors/CookieContext. Minimal BaseRestClient (base URI + shared filters). VERIFY: 14 API+DB tests + web @ApiLogin live. ← checkpoint/commit.
- 2b: port custom logging infra — RestAssuredRequestLogger (Filter, slf4j+Allure, blacklist), RequestLogModifier/ResponseLogModifier + FilterableRequestSpecificationOverrides/ResponseOptionsOverrides (proxy mask, log-view≠wire-view), InterfaceProxy, BaseHttpApi-style per-call log control. Use to MASK auth token + password in Allure logs. AllureHelper helper. Re-verify.
- Cleanup: remove retrofit bundle (build.gradle) + catalog entries.

## Verification net (characterization)
14 API+DB e2e: CountryGrpcTest(3 — gRPC, unaffected), UserdataGrpcTest(3 — gRPC), PhotoGrpcTest(5 — gRPC), GatewayRestTest(4 — REST via gateway client + OAuth login), + web @ApiLogin (ApiLoginCallback OAuth flow). GatewayRestTest + web exercise the full OAuth + GatewayApiClient + UserService(register/addFriend via UserdataRestClient). Green = equivalence.
Stack: auth+country+userdata+photo+gateway (local), MySQL+Kafka up.
