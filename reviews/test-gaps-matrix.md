# Rangiffler test-coverage gap analysis (2026-06-11)

Compared my `com.elakov.rangiffler` e2e module against 6 sibling forks + 2 heisenbug
JUnit demos + junit-pioneer. (unownp/rangiffler = no tests, skipped.)

## My current state (exact)

| Layer | My count | Detail |
|---|---|---|
| WEB | 22 @Test / 9 classes | auth-heavy: LoginError(5), Profile(4), RegistrationError(4); Photo/Friends/Logout/Registration = 1 each |
| API (gRPC) | **0** | `BaseGrpcTest` exists, 0 @Test, no subclasses |
| API (REST) | **0** | `BaseRestTest` exists, 0 @Test, no subclasses |
| DATABASE | **0** | no dedicated DB-assertion tests |
| KAFKA | 2 | producer + consumer (done this session) |
| `e2e/PhotoE2ETest` | **0** | empty scaffold |

My annotations: @Env @GrpcTest @KafkaTest @RestTest @Logger @LogP6Spy @ApiLogin @WebTest
@CreateUser @CreateUserInDB @CreatePhoto @CreateFriend (flat, NOT nested/composable).
My callbacks: TestSuiteCallback(+CloseableResource ✓), BrowserConfigExtension, KafkaExtension,
AllureLogAttachCallback, ErrorLoggerCallback, EventLoggerCallback, EnvironmentExecutionCondition,
CreateUserInAuthDatabaseCallback, CreateUserCallback, ApiLoginCallback.

## Field reference (what the forks have)

| Repo | WEB | API-gRPC | API-gql/REST | DB | Kafka | Standout |
|---|---|---|---|---|---|---|
| onehundredtwenty-ninth | 13 | ~20 | ~18 | woven | 2 | Guice DI, AssertJ+Soft per transport, Freemarker gql, SuiteExtension |
| andreyzavrichko | ~15 | ~30 (+validation suites) | 7 | poll | 1 | gRPC error/validation suites, Guice, allure-docker, @Order kafka |
| sashkir7 | 11 | 5 | — | embedded | (rabbit) | @WithPartner recursive graph, eventual-consistency polling |
| kpetukh | 6 | 3 | — | infra-only | 0 | AllureId dual-namespace store, p6spy→Allure |
| AydarSS | 5 | 3 | 3 | woven | 2 | abstract-extension inheritance, param resolvers, both kafka tests |
| Clattoo | 5 | 4 | 6 | setup-only | 0 | Atomikos XA, ThreadSafeEntityManager, @DisabledByIssue |

## GAP 1 — API + DATABASE layers are empty (biggest)

Every fork has 5–50 API tests; I have 0. Concrete gRPC test classes needed per service:
- **country/geo**: GetAllCountries (assert 175), GetCountryByCode, GetNonExistentCountry (NOT_FOUND)
- **photo**: Create/Update/Delete/GetAllPhotos/GetFriendsPhotos, modify-other-user (PERMISSION), modify-nonexistent
- **userdata**: GetCurrentUser, GetAllUsers/People, friends list, income/outcome invitations, updateUserFriendship, illegal-friendship
- **gRPC validation suite** (andreyzavrichko pattern): assert StatusRuntimeException codes — now testable against my new GrpcExceptionHandler
- **REST (gateway)**: same surface through the gateway HTTP API + JWT
- **DATABASE**: hibernate-repo assertions after each mutation (the data IS there — `UserdataRepositoryImpl` etc. already exist, used only by kafka consumer test)

## GAP 2 — WEB coverage thin

Have auth + login errors. Field also covers: AddPhoto/EditPhoto/DeletePhoto/LikePhoto, FriendsList/People/Income/Outcome invitations/UpdateFriendship, Statistics(map coloring), UpdateUser. My Photo=1, Friends=1 → expand to ~12.

## GAP 3 — annotations/callbacks to borrow

| Pattern | Source | I have? | Value |
|---|---|---|---|
| Nested data-factory `@CreateUser(friends=@Friend(...), photos=@WithPhoto(...))` | all forks | flat only | build whole user+friends+photos graph from ONE annotation |
| `@Token`/`@Cookie` ParameterResolver injection | heisenbug, 129th | no | inject auth context into test signature |
| SPI auto-registration `META-INF/services/...Extension` | most | no | global extensions without @ExtendWith |
| ContextHolderExtension (ThreadLocal bridge to interceptors) | heisenbug, AydarSS | no | OkHttp interceptor writes OAuth code into test Store |
| Custom AssertJ + SoftAssertions per transport | 129th | no | fluent `assertThat(user).hasUsername().firstNameIsNull()` |
| Custom Selenide conditions (PhotoCondition/FriendCondition) | sashkir7, kpetukh, AydarSS | no | domain-aware element matching (base64 photo compare) |
| @ScreenShotTest + image-diff visual regression | andreyzavrichko, Clattoo | no | map-coloring / avatar pixel assertions |
| p6spy → Allure SQL attachment pipeline | kpetukh, Clattoo, 129th | annotation only (@LogP6Spy) | every SQL formatted+attached to report |
| @DisabledByIssue + GitHub-issue ExecutionCondition | Clattoo, heisenbug | no | auto-skip tests whose tracking issue is open |
| Kafka sync primitive (MapWithWait/WaitForOne) | andreyzavrichko, AydarSS | Awaitility poll | tighter than polling; mine works though |
| allure-docker upload extension (env-gated) | andreyzavrichko, heisenbug | no | CI report push |

## GAP 4 — junit-pioneer techniques (library or port)

1. **@RetryingTest** mechanics — flaky web/gRPC retry with `onExceptions` filter (retry Selenide TimeoutException / gRPC UNAVAILABLE, fail-fast on assertions). Highest ROI. `TestTemplateInvocationContextProvider` + Iterator + TestExecutionExceptionHandler, `@Execution(SAME_THREAD)`.
2. **PioneerAnnotationUtils** — annotation lookup up hierarchy + enclosing classes + meta-annotations. Makes my meta-annotations resolve class-level defaults + method overrides correctly (`findClosestEnclosingAnnotation`, `findAnnotatedAnnotations`).
3. **ExtensionContext.Store over ThreadLocal** — formalize my context holders, parallel-safe + visible to ParameterResolver (`AbstractEntryBasedExtension` save/restore template).
4. **@CartesianTest** — data matrices (role × country × friend-state) instead of hand-written tuples; one provider class implements both ArgumentsProvider + CartesianParameterArgumentsProvider.
5. **Platform TestExecutionListener via ServiceLoader** for true suite-wide aggregation (not Jupiter callback); cross-component signaling via `publishReportEntry`.

## Recommended order
1. **API gРpc tests + DB assertions** (biggest hole; infra already exists) — country → userdata → photo, ~40 tests
2. **Nested data-factory annotations** — refactor flat @CreateUser into composable graph (unblocks rich API/web setup)
3. **gRPC validation suite** — exercise the new GrpcExceptionHandler
4. **Expand WEB** — photo CRUD, friends/invitations, statistics (~10 classes)
5. **@RetryingTest + PioneerAnnotationUtils** — stability + cleaner meta-annotations
6. Custom AssertJ/SoftAssertions, Selenide conditions, p6spy→Allure, @ScreenShotTest — polish

---

## STATUS UPDATE (2026-06-11) — unit-test layer DONE

GAP №2 (unit tests, was 0) closed. 54 tests / 12 classes across all 5 services, 0 failures.
Commits: 73e749b (auth), bf84352 (country/photo/userdata), 06376b0 (gateway).

- auth (11): EqualPasswordsValidator, RangifflerUserPrincipal (pure); UserDetailsService
  (Mockito) + WithFakeObjects (EO fake, in-memory FakeUserRepository); UserService (Mockito)
- country (3): GrpcCountryService (stream all / by-code / NOT_FOUND)
- photo (4): GrpcPhotoService (getPhotos/addPhoto/deletePhoto/friendsPhotos)
- userdata (13): UserDataService (9 methods, happy+NotFound) + KafkaUserService
- gateway (23): GrpcExceptionHandler (14, parametrized Status→HTTP — closes the
  not-runtime-tested caveat), CountryJson (4), PhotoJson (5)

Infra: spring-boot-starter-test (BOM) + useJUnitPlatform() added to all 5 modules.

Deliberately NOT unit-tested (low value / wrong layer):
- thin gRPC client wrappers (GrpcCountryClient etc.) — 1-line stub passthrough
- controllers — thin delegators (belong to @WebMvcTest / e2e)
- CORS/CSRF config + filters — Spring wiring, better covered by integration
- RestUserdataClient (WebClient) — needs MockWebServer; deferred

Still open from prior analysis: GAP №1 (API gRPC + DB e2e tests), nested data-factory
annotations, @RetryingTest, custom AssertJ/SoftAssertions, Selenide conditions.

---

## BACKLOG (deferred, separate branch)

- **Structured logging + distributed tracing (logging skill, option B)** — Boot 4.1
  supports native structured logging (`logging.structured.format.console: ecs`,
  no extra deps) + a `human-logs` profile for local readability. Real value is a
  `requestId`/`traceId` MDC propagated gateway -> gRPC metadata -> 3 services so
  the 4 services' logs correlate (a format without correlation is just structured
  noise). Deferred from the niffler-parity branch to keep the merge focused;
  needs careful gRPC metadata propagation. Motivated by this session: hours lost
  grepping noisy plain-text logs (Kafka trace, OAuth exchange, test stdout).
- **Web e2e against production frontend build** — run UI tests against the nginx
  static build, not `npm start`; the dev server's react-refresh-overlay iframe
  intercepts Selenide clicks (false failures in ProfileTest 1014/1015).
- **Nested data-factory annotations** — @CreateFriend recursion (friends-of-friends)
  + friendship-status params; current flat graph covers friends/invitations/photos.
