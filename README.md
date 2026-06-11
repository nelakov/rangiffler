<p align="center">
<img src="utils/Images/download.svg" width="250" alt="Rangiffler logo"/>
</p>

<h1 align="center">Rangiffler</h1>

<p align="center"><i>I haven't been everywhere, but it's on my list</i></p>

Rangiffler is a travel-tracking application built on a microservice architecture: upload photos from your trips, see them as marks on a world map, add friends and follow their journeys. The name combines *Rangifer* (the reindeer genus) with a love for wandering.

The project doubles as a playground for a production-style test harness: Selenide UI tests, gRPC/API tests, JUnit extensions for test data setup, and Allure reporting.

## Table of Contents

- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Service Ports](#service-ports)
- [Modules](#modules)
- [Prerequisites](#prerequisites)
- [Running Locally](#running-locally)
- [Observability](#observability)
- [Testing](#testing)
- [Test Utilities](#test-utilities)
- [Known Issues](#known-issues)

## Technology Stack

| Area | Technology |
|------|-----------|
| Language / build | Java 25, Gradle 9.4.1 (version catalog in `gradle/libs.versions.toml`) |
| Framework | Spring Boot 4.1 (Spring Framework 7) |
| Security | Spring Security 7 — OAuth2 Authorization Server (auth), JWT Resource Server (gateway) |
| Inter-service RPC | [Spring gRPC](https://docs.spring.io/spring-grpc/reference/) 1.0 (official starters), gRPC 1.82, Protobuf 4.35 |
| Persistence | Spring Data JPA, Hibernate 7, MySQL 8 |
| Messaging | Kafka (auth publishes user registration events, userdata consumes) |
| Frontend | React 19, TypeScript 6, MUI 9, webpack 5 |
| Auth UI | Thymeleaf (login/registration pages served by auth) |
| Testing | JUnit 6, Selenide, REST/gRPC API tests, Allure reports |
| Packaging | Docker (Bmuschko gradle plugin, `dockerBuild` task per module) |

## Architecture

```
Browser ──► rangiffler-client (React SPA, :3001)
                 │ REST + JWT
                 ▼
            rangiffler-gateway (:8080) ◄──JWT issuer──► rangiffler-auth (:9000)
                 │ gRPC                                       │ Kafka
     ┌───────────┼────────────┐                          (user events)
     ▼           ▼            ▼
  country      photo       userdata
  (:9011)     (:9021)     (:9030 REST / :9031 gRPC)
```

- **auth** issues JWTs (OAuth2 Authorization Code flow + form login + registration) and emits a Kafka event per registered user.
- **gateway** validates JWTs and fans client REST calls out to backend services: gRPC to country/photo, REST to userdata.
- **photo** is also a gRPC *client* of country and userdata (resolves photo country, fetches friend lists for the friends feed).

## Service Ports

| Service  | HTTP | gRPC | Database (MySQL schema) |
|----------|------|------|-------------------------|
| auth     | 9000 | —    | rangiffler-auth         |
| country  | 9010 | 9011 | rangiffler-country      |
| photo    | 9020 | 9021 | rangiffler-photo        |
| userdata | 9030 | 9031 | rangiffler-userdata     |
| gateway  | 8080 | —    | —                       |
| client   | 3001 | —    | —                       |

## Modules

| Module | Purpose |
|--------|---------|
| `rangiffler-auth` | OAuth2 Authorization Server: login/registration UI, JWT issuing, Kafka producer |
| `rangiffler-gateway` | API gateway: JWT validation, REST↔gRPC fan-out |
| `rangiffler-country` | Country reference data (gRPC server) |
| `rangiffler-photo` | Photo storage with country association (gRPC server + gRPC client) |
| `rangiffler-userdata` | User profiles and friendship management (gRPC server + REST) |
| `rangiffler-grpc-common` | Shared `.proto` contracts; stubs generated into `build/generated/source/proto/` |
| `rangiffler-client` | React SPA |
| `rangiffler-e-2-e-tests` | Selenide UI tests + API tests with Allure reporting |

## Prerequisites

- **Java 25** (toolchain enforced by Gradle)
- **Docker** — for MySQL and Kafka
- **Node.js + npm** — for the frontend

Start the infrastructure:

```bash
# MySQL (databases are auto-created on first connect)
docker run -d --name rangiffler-mysql \
  -e MYSQL_ROOT_PASSWORD=secret -p 3306:3306 mysql:8.0

# Kafka (KRaft, no Zookeeper)
docker run -d --name rangiffler-kafka \
  -e KAFKA_NODE_ID=1 -e KAFKA_PROCESS_ROLES=broker,controller \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 \
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
  -e KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1 \
  -e KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS=0 \
  -e CLUSTER_ID=MkU3OEVBNTcwNTJENDM2Qk \
  -p 9092:9092 apache/kafka:latest
```

## Running Locally

Build everything once:

```bash
./gradlew clean build -x test
```

Start services **in this order** (gateway discovers the JWT issuer from auth at startup):

```bash
# 1. Auth — must be up before the gateway
./gradlew :rangiffler-auth:bootRun --args='--spring.profiles.active=local'

# 2. Backend services — any order
./gradlew :rangiffler-country:bootRun --args='--spring.profiles.active=local'
./gradlew :rangiffler-userdata:bootRun --args='--spring.profiles.active=local'
./gradlew :rangiffler-photo:bootRun --args='--spring.profiles.active=local'

# 3. Gateway
./gradlew :rangiffler-gateway:bootRun --args='--spring.profiles.active=local'

# 4. Frontend
cd rangiffler-client && npm install && npm start
```

The app opens at http://localhost:3001.

Frontend checks:

```bash
cd rangiffler-client
npm run lint          # eslint (flat config) + prettier
npm run build:docker  # production webpack build
```

## Observability

### Structured logging

Each backend service logs **plain text** under the `local`/default profile (readable in a `bootRun` terminal) and **ECS JSON** under the `docker` profile (production / containers), using Spring Boot 4 native structured logging — no extra dependencies. JSON logs parse directly with `jq`:

```bash
# follow a service's container logs, pretty
docker logs -f photo.rangiffler.dc | jq .

# only errors
docker logs gateway.rangiffler.dc | jq 'select(.log.level == "ERROR")'
```

To get JSON locally (e.g. for log analysis), override the format on any service:

```bash
./gradlew :rangiffler-country:bootRun --args='--spring.profiles.active=local --logging.structured.format.console=ecs'
```

### Request tracing across the gRPC mesh

A single **request-id** correlates the logs of all four services. The gateway takes the `X-Request-Id` header (or mints a UUID), echoes it on the response, and forwards it as gRPC metadata (`x-request-id`) on every downstream call; country / userdata / photo read it back into MDC, so it rides into each ECS log line as a `requestId` field. Follow one request end-to-end:

```bash
# any gateway request (e.g. GET /countries, with a Bearer JWT); the gateway echoes X-Request-Id back
curl -H 'X-Request-Id: trace-123' -H 'Authorization: Bearer <jwt>' http://localhost:8080/countries

# then, across every service's JSON logs:
docker logs photo.rangiffler.dc | jq 'select(.requestId == "trace-123")'
```

The shared contract (MDC key + header) lives in `rangiffler-grpc-common` (`tracing/RequestIdSupport`); interceptors are registered per service via `@GlobalServerInterceptor` / `@GlobalClientInterceptor`. Each non-infra gRPC call also emits one INFO access-log line carrying the id (`grpc.health.*` / `grpc.reflection.*` are skipped to avoid probe noise).

## Testing

E2E tests (Selenide + JUnit 6 + Allure) require the full stack from [Running Locally](#running-locally) plus Chrome:

```bash
# all e2e tests
./gradlew :rangiffler-e-2-e-tests:test

# subset by class name
./gradlew :rangiffler-e-2-e-tests:test --tests '*LoginTest'

# Allure report
./gradlew :rangiffler-e-2-e-tests:allureServe

# Kafka tests (registration event published by auth, consumed by userdata)
./gradlew :rangiffler-e-2-e-tests:test --tests '*Kafka*' -Denv=local
```

Test data is provisioned through JUnit 5-style extensions — `@ApiLogin`, `@CreateUser`, `@CreatePhoto`, `@CreateFriend` — which register users via the Auth API and create entities over gRPC, so tests never click through setup flows.

> **Note:** tests annotated with `@Env` are silently skipped unless `-Denv=local` (or a matching `env` environment variable) is passed.

> **Web tests:** serve the frontend with `npm run start:e2e` (production webpack build, no react-refresh overlay) instead of `npm start` — the dev server's overlay iframe intercepts Selenide clicks.

Browser configuration lives in `rangiffler-e-2-e-tests/src/test/resources/config/local/web_local.properties`.

## Test Utilities

Two helpers (`com.elakov.rangiffler.helper.*`) make e2e assertions richer in the Allure report.

### JsonComparator — structural JSON diff

Asserts a JSON body equals an expected one and attaches a side-by-side **Actual / Expect HTML diff** to the Allure report (differing paths highlighted), on both pass and fail — so a failure shows exactly what mismatched.

```java
// REST (raw body)
new JsonComparator()
        .assertThatJson(gatewayApiClient.currentUserRaw(token))
        .ignorePaths("id")                       // volatile/server-generated paths
        .equalsToJson(expectedJson);

// A model object (serialized via Jackson) — e.g. a gRPC response mapped to a record
new JsonComparator()
        .assertThatObject(PhotoJson.fromGrpcMessage(photo))
        .ignorePaths("id", "photo", "country.id")
        .equalsToJson(expectedJson);
```

Comparison is backed by **json-unit**; `assertThatObject` serializes the object first. For a protobuf message with no model mapping, convert it with `JsonFormat.printer().includingDefaultValueFields().print(message)` and use `assertThatJson`.

### AllureSoftSteps — soft assertions as Allure steps

Runs every check (not stopping at the first failure), each as its own Allure step; one failure rethrows the original cause, more than one throws a single error summarizing the count.

```java
new AllureSoftSteps()
        .add("count matches the DB", () -> assertThat(countries).hasSameSizeAs(fromDb))
        .add("contains FJ and GE",   () -> assertThat(countries).extracting(Country::getCode).contains("FJ", "GE"))
        .execute();
```

Use it where a test has **2+ independent** checks (so one run reports them all). For a single assertion or an atomic comparison, a plain assert is clearer. When a step depends on an earlier one (e.g. `list.get(0)` after a size check), evaluate that access **inside** the step lambda so an empty/null upstream surfaces as that step's own failure.

## Known Issues

- **E2E-in-Docker needs remote-browser wiring.** The compose stack (`docker-compose.test.yml`) is current, but the test code configures local Chrome only — Selenide `Configuration.remote` support for Selenoid is not implemented yet, so the `rangiffler-e-2-e` container cannot pass.
- Lint and type-check surface pre-existing findings (MUI Grid v1 `item` props, a few `react-hooks` violations) that are tracked but not yet fixed.

---

<p align="center"><i>Six month vacation, twice a year</i></p>

<p align="center">
<img src="utils/Images/mimino_rangiffler.jpg" width="600" alt="Rangiffler"/>
</p>
