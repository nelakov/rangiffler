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
- [Testing](#testing)
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

## Testing

E2E tests (Selenide + JUnit 6 + Allure) require the full stack from [Running Locally](#running-locally) plus Chrome:

```bash
# all e2e tests
./gradlew :rangiffler-e-2-e-tests:test

# subset by class name
./gradlew :rangiffler-e-2-e-tests:test --tests '*LoginTest'

# Allure report
./gradlew :rangiffler-e-2-e-tests:allureServe
```

Test data is provisioned through JUnit 5-style extensions — `@ApiLogin`, `@CreateUser`, `@CreatePhoto`, `@CreateFriend` — which register users via the Auth API and create entities over gRPC, so tests never click through setup flows.

> **Note:** tests annotated with `@Env` are silently skipped unless `-Denv=local` (or a matching `env` environment variable) is passed.

Browser configuration lives in `rangiffler-e-2-e-tests/src/test/resources/config/local/web_local.properties`.

## Known Issues

- **Docker Compose flow is stale.** `docker-compose.yml` / `docker-compose.test.yml` (Selenoid + allure-docker-service pipeline) reference images from a previous project iteration (`rangiffler-currency`, `rangiffler-spend`) and provision PostgreSQL while the services' `docker` profile expects MySQL. Local launch is the supported path until the compose stack is reworked.
- Lint and type-check surface pre-existing findings (MUI Grid v1 `item` props, a few `react-hooks` violations) that are tracked but not yet fixed.

---

<p align="center"><i>Six month vacation, twice a year</i></p>

<p align="center">
<img src="utils/Images/mimino_rangiffler.jpg" width="600" alt="Rangiffler"/>
</p>
