# integrationtests → rangiffler e2e: feature inventory

Source: `/Users/nicke/Repositories/jvm/integrationtests` (ru.beeline.oms.autotest — OMS REST/Temporal/Workflow autotest framework, JUnit 6 + Allure 4 + rest-assured 6 + json-unit).
Target: `rangiffler-e-2-e-tests` (Selenide + JUnit 6 + Allure + Retrofit REST + gRPC clients).

Portability legend: 🟢 drop-in / high value · 🟡 needs adaptation (rest-assured→Retrofit, etc.) · 🔴 Beeline-domain, not portable.

## A. JSON comparison + visual diff  🟢 (user's #1 ask)
- **JsonComparator** (`helpers/comparator/JsonComparator`): fluent `assertThatJson(json).ignorePaths(...).equalsToJson(expect)`. Wraps **json-unit-assertj**; on BOTH pass and fail builds a side-by-side tree diff and attaches it as HTML to Allure.
- Custom diff renderer: `comparator/{Node,NodePair,Tree,Line,Text,NodeType,Staff}` + `report/html/DiffReport` + `report/style/BrightTextStyle` + `listeners/DiffResultListener`. Renders which paths differ, colored.
- **AllureDiffReportHelper**: attaches the rendered HTML diff.
- Dep: `net.javacrumbs.json-unit:json-unit(-assertj):5.1.2`, guava (HtmlEscapers).

## B. Allure helpers  🟢
- **AllureHelper**: static facade — `step(name, runnable)` (Allure.step + slf4j log), `addStepParameter`, `attachText/Json/Xml/Html`.
- **AllureSoftSteps**: soft assertions as Allure steps — collect N steps, run all, aggregate failures (1 → rethrow original; >1 → `SoftStepError` w/ count); ThreadLocal `seenErrors` reports each error once across nested scopes.

## C. Per-test logs rendered in Allure  🟢 (user's ask)
- **AllureLogAppender** (logback `AppenderBase`): buffers each test's log lines (ThreadLocal StringBuilder) as color-coded HTML (WARN/ERROR/DEBUG/STEP colors) + auto-resize-iframe script; `getHtmlAndClear()`.
- **AllureLogsAttach** (JUnit `BeforeEach/AfterEach`): clears appender before test, attaches `logs.html` to Allure after → the rendered per-test log.
- **AllureStepListener** (Allure SPI `StepLifecycleListener` via `META-INF/services`): logs every Allure step start with `STEP > ` prefix so steps appear inline in the log stream.
- Root `logs/` dir → logback file appender writes per-run log files too.

## D. Retrying  🟡 (rangiffler already has @RetryingTest)
- **BaseConditionalRetrying** (`InvocationInterceptor`): conditional retry — abstract `skipError(throwable, ctx)`, `maxRetryCount()=3`, each retry is an Allure step "Попытка № N", reflectively resets Jupiter's `invokedOrSkipped`. Concrete: `WfDevFlakyProblems`.
- vs rangiffler `@RetryingTest` (TestTemplate + Lifecycle handler): theirs wraps the WHOLE method (incl. setup) via interceptor, no @TestTemplate; ours skips/aborts via TestTemplate. Comparable; theirs is the InvocationInterceptor alternative.

## E. Feature flags  🔴
- `@UseFeatureFlag/@IgnoreFeatureFlag` + `FeatureFlagsHandler/Context/Remind/AlertUriListener` — gate/toggle tests by Unleash flag w/ reminders + alerting. git_unleash-specific.

## F. API layer (investigated per request)
- **Api** (`api/Api`) 🟡: static ThreadLocal-cached accessors to per-domain clients (sqlStub, soapMocks, wfApi, temporal, mocks, productOrder, gitUnleash, shoppingCart). Thread-local singleton = parallel-safe. Pattern portable; the clients are not.
- **BaseHttpApi<SELF>** 🟡: rest-assured base w/ per-call log control — enable/disable, request/response, attach-to-allure, LogDetail, request/response **modifiers** (mask/trim bodies), header **blacklist**, scoped `withLogSettings(settings, execute)` override+restore, `addCommonFilter` global filter. CRTP fluent. **Concept is gold; impl is rest-assured (rangiffler uses Retrofit) → rewrite as Retrofit/OkHttp interceptor.**
- **api/logging/** 🟡: `RestAssuredRequestLogger` (Filter) — logs req/resp, attaches to Allure, header blacklist, log modifiers, pretty-print; `FilterableRequestSpecificationOverrides`, `ResponseOptionsOverrides`, `RequestLogModifier/ResponseLogModifier`.
- Domain clients (Workflow/Temporal/SqlStub/SoapMocks/Mocks/ProductOrder/GitUnleash/ShoppingCart) 🔴: OMS-specific.

## G. Misc helpers
- **JacksonPool** 🟢: pooled/configured ObjectMapper builder (`defaultMapper()`).
- **Wait** 🟢: custom polling/wait util.
- **InterfaceProxy / DefaultMethodInvoker** 🟡: dynamic interface proxy (used by rest-assured log overrides).
- **DataHandler** 🟡: static-resource test-data loader.
- **listeners/ErrorLogger, EventLogger, WfQueueContextHandler** 🟡/🔴.

## Recommendation (port order)
1. **C — per-test logs → Allure** (AllureLogAppender + AllureLogsAttach + AllureStepListener). Self-contained, logback+Allure already present. Immediate debugging value.
2. **A — JsonComparator + HTML diff** (+ json-unit dep). Highest value for API/gRPC response asserts; the "see exactly what differs" feature.
3. **B — AllureHelper + AllureSoftSteps**. Small, multiplies value of A+C.
4. **F(concept) — Retrofit request/response logger → Allure** (rewrite BaseHttpApi log-control idea as an OkHttp interceptor). Medium effort.
5. Skip: E (feature flags), domain API clients, Temporal/SQL/SOAP — Beeline-specific.
