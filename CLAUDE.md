# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build all modules (skip tests)
./gradlew clean build -x test

# Build single module
./gradlew :rangiffler-auth:build

# Run a service locally (needs build first; all services need local profile)
./gradlew :rangiffler-auth:bootRun --args='--spring.profiles.active=local'

# Run e2e tests (requires all services + UI running)
./gradlew :rangiffler-e-2-e-tests:test

# Run a subset of e2e tests by class name pattern
./gradlew :rangiffler-e-2-e-tests:test --tests '*LoginTest'

# Build Docker image for a module
./gradlew :rangiffler-auth:dockerBuild

# Frontend (React/webpack)
cd rangiffler-client && npm install && npm start

# Lint frontend
cd rangiffler-client && npm run lint
```

## Infrastructure (local dev)

All backend services require MySQL on `localhost:3306` (user: `root`, password: `secret`) and Kafka on `localhost:9092`.

```bash
docker run -d --name rangiffler-mysql -e MYSQL_ROOT_PASSWORD=secret -p 3306:3306 mysql:8.0
docker run -d --name rangiffler-kafka -e KAFKA_NODE_ID=1 -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
  -e KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS=0 \
  -e CLUSTER_ID=MkU3OEVBNTcwNTJENDM2Qk -p 9092:9092 apache/kafka:latest
```

Databases are auto-created via `?createDatabaseIfNotExist=true` in JDBC URLs.

## Startup Order

Services must start in this order (auth issues JWT, gateway validates it):
1. MySQL + Kafka (infrastructure)
2. `rangiffler-auth` (must be healthy before gateway starts)
3. `rangiffler-country`, `rangiffler-userdata`, `rangiffler-photo` (any order)
4. `rangiffler-gateway` (depends on auth for JWT issuer-uri discovery)
5. `rangiffler-client` (depends on gateway)

## Architecture

Microservices travel app with OAuth2 authentication:

```
Browser → rangiffler-client (:3001)
              ↓
         rangiffler-gateway (:8080) ──JWT validation──→ rangiffler-auth (:9000)
              ↓ gRPC                                         ↓ Kafka
    ┌─────────┼──────────┐                              (user events)
    ↓         ↓          ↓
country    photo     userdata
(:9011)   (:9021)   (:9030/:9031)
  gRPC      gRPC      gRPC+REST
```

- **rangiffler-auth** — OAuth2 Authorization Server (Spring Authorization Server), form login, user registration, JWT issuing
- **rangiffler-gateway** — API gateway, validates JWT, proxies REST to gRPC services
- **rangiffler-country** — Country reference data (gRPC server)
- **rangiffler-photo** — Photo storage with country association (gRPC server, gRPC client to country+userdata)
- **rangiffler-userdata** — User profiles, friend relationships (gRPC server + REST)
- **rangiffler-grpc-common** — Shared `.proto` definitions, generated stubs in `build/generated/source/proto/`
- **rangiffler-e-2-e-tests** — Selenide UI tests + API tests with Allure reporting
- **rangiffler-client** — React SPA (webpack, TypeScript, MUI 9)

## Key Technical Details

- **Java 25** with Gradle 9.4.1 toolchain
- **Spring Boot 4.1.0** (Framework 7, Security 7), gRPC 1.82.0, Protobuf 4.35.0 — authoritative source: `gradle/libs.versions.toml`
- **gRPC integration**: official `org.springframework.grpc` starters (replaced dead net.devh). Server is any `@Service` extending the generated `*ImplBase`; client stubs are `@Bean`s wired via `GrpcChannelFactory` (see `config/GrpcClientsConfig.java` in photo/gateway). Config prefix is `spring.grpc.*`, channels under `spring.grpc.client.channels.<name>`
- **Profiles**: `local` (localhost URLs, MySQL) and `docker` (container DNS; stale — compose provisions PostgreSQL but service URLs are `jdbc:mysql://`)
- **Model classes use Java records** (not Lombok @Data) — accessors are `obj.field()` not `obj.getField()`
- **JPA entities still use Lombok @Data** (Hibernate requires mutability)
- **Docker plugin**: Bmuschko `docker-remote-api` (replaced Palantir). Task name: `dockerBuild` (not `docker`)
- **Version catalog**: `gradle/libs.versions.toml` — all dependency versions centralized
- **gRPC codegen**: proto files in `rangiffler-grpc-common/src/main/proto/`, output to `build/generated/source/proto/` (protobuf plugin 0.10.0 removed configurable `generatedFilesBaseDir`)

## Service Ports (local profile)

| Service  | HTTP | gRPC | Database                |
|----------|------|------|-------------------------|
| auth     | 9000 | —    | rangiffler-auth         |
| country  | 9010 | 9011 | rangiffler-country      |
| photo    | 9020 | 9021 | rangiffler-photo        |
| userdata | 9030 | 9031 | rangiffler-userdata     |
| gateway  | 8080 | —    | —                       |
| client   | 3001 | —    | —                       |

## E2E Tests

Tests use Selenide (Chrome) + Allure. They require:
1. All 5 backend services running with `local` profile
2. UI running on `:3001`
3. Chrome browser installed (version auto-detected by Selenium Manager)

Browser config: `rangiffler-e-2-e-tests/src/test/resources/config/local/web_local.properties`

Test data setup uses JUnit 5 extensions (`@ApiLogin`, `@CreateUser`, `@CreatePhoto`, `@CreateFriend`) that register users via Auth API and create data via gRPC.

## Code Style

- Prefer Java `var` over Lombok `val` — no Lombok for type inference (Java 10+).
- Use `private final` for instance fields (page objects, components), `private static final` for class-level constants.
- Name wrapper/client classes as `*Client` not `*Utils` — reflects role, not implementation.
- Page object assertion methods: `should...()` for void assertions (throw on failure), `is...Visible()` for boolean queries.
- DRY: deduplicate **knowledge** (how to connect, how to generate dates), not **test code** (tests stay readable as standalone specs).
- Parameterize via constructor when subclasses differ in **values**, not behavior — avoid unnecessary abstract methods.

## Gotchas

- **gradlew CRLF**: On macOS, `./gradlew` may fail with "bad interpreter" if it has Windows line endings. Fix: `sed -i '' 's/\r$//' gradlew`
- **Frontend env files**: webpack selects `.env.dev` / `.env.docker` / `.env.test` based on `NODE_ENV` (see `webpack.config.js` line 49). `npm start` uses `NODE_ENV=development` → `.env.dev`
- **OAuth2 token exchange**: Token endpoint requires `application/x-www-form-urlencoded` POST body (not query params). Changed in Spring Authorization Server 1.4+
- **react-svg-worldmap 2.x**: Countries with `value: 0` in data array are colored on the map. Only include countries with actual photos. Since 2.0.2 types (`CountryContext` etc.) are exported from the package root — `react-svg-worldmap/dist/types` no longer exists.
- **MUI 9 icon renames**: legacy un-suffixed outline names dropped — `AddCircleOutline` → `AddCircleOutlined`, `DeleteOutline` → `DeleteOutlined`.
- **eslint**: flat config (`eslint.config.mjs`); stay on eslint 9.x — eslint-plugin-react is runtime-incompatible with eslint 10.
- **userdata gRPC port**: userdata is a servlet web app, so `spring.grpc.server.servlet.enabled: false` is required to keep gRPC on its own port (9031) instead of being served through HTTP (9030).
- **@Env-annotated e2e tests**: silently disabled unless `-Denv=local` (or matching `env` environment variable) is passed — they show as skipped, not failed (`EnvironmentExecutionCondition`).
- **Web e2e frontend**: serve with `npm run start:e2e` (production-mode webpack at :3001), NOT `npm start`. The dev server's react-refresh overlay iframe (z-index max) intercepts Selenide clicks → false `element click intercepted` failures (e.g. ProfileTest). `start:e2e` uses `NODE_ENV=e2e` → `.env.e2e` (localhost backend) + production mode, so no react-refresh.
