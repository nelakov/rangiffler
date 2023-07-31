## **Технологии, использованные в Rangiffler**

- [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)
- [Spring OAuth 2.0 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Spring data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Web](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#spring-web)
- [Spring actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Spring gRPC by https://github.com/yidongnan](https://yidongnan.github.io/grpc-spring-boot-starter/en/server/getting-started.html)
- [Spring web-services](https://docs.spring.io/spring-ws/docs/current/reference/html/)
- [Docker](https://www.docker.com/resources/what-container/)
- [Docker-compose](https://docs.docker.com/compose/)
- [Postgres](https://www.postgresql.org/about/)
- [React](https://ru.reactjs.org/docs/getting-started.html)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Jakarta Bean Validation](https://beanvalidation.org/)
- [Jakarta JAXB](https://eclipse-ee4j.github.io/jaxb-ri/)
- [JUnit 5 (Extensions, Resolvers, etc)](https://junit.org/junit5/docs/current/user-guide/)
- [Retrofit 2](https://square.github.io/retrofit/)
- [Allure](https://docs.qameta.io/allure/)
- [Selenide](https://selenide.org/)
- [Selenoid & Selenoid-UI](https://aerokube.com/selenoid/latest/)
- [Allure-docker-service](https://github.com/fescobar/allure-docker-service)
- [Java 19](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Gradle 7.6](https://docs.gradle.org/7.6/release-notes.html)
- And much more:)

**Минимальные предусловия для работы с проектом Rangiffler**

#### 1. Установить docker (Если не установлен)

Используется docker для БД (MySql)
Микросервисы будут запускаться в едином docker network при помощи docker-compose

[Установка на Windows](https://docs.docker.com/desktop/install/windows-install/)

[Установка на Mac](https://docs.docker.com/desktop/install/mac-install/) (Для ARM и Intel разные пакеты)

[Установка на Linux](https://docs.docker.com/desktop/install/linux-install/)

После установки и запуска docker daemon необходимо убедиться в работе команд docker, например `docker -v`:

```bash
docker -v
```

#### 2. Спуллить контейнер mysql версии 8.0.33

```posh
docker pull mysql:8.0.33
```

После `pull` вы увидите спуленный image командой `docker images`

```posh
❯ docker images
REPOSITORY                  TAG       IMAGE ID       CREATED         SIZE
mysql                       8.0.33    a5e6f938c138   9 days ago      587MB
```

#### 3. Создать volume для сохранения данных из БД в docker на вашем компьютере

```bash
docker volume create mysqldata
```

#### 4. Запустить БД командой

```bash
docker run --name rangiffler-all -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -v mysqldata:/var/lib/mysql -d mysql:8.0.33
```

#### 5. Установить одну из программ для визуальной работы с MySql

Например, DBeaver или Datagrip

#### 6. Подключиться к БД postgres (host: localhost, port: 3306, user: root, pass: secret)

#### 7. Установить Java версии 17 или новее. Это необходимо, т.к. проект не поддерживает версии <17

Версию установленной Java необходимо проверить командой `java -version`

```posh
❯ java -version
java version "20.0.1" 2023-04-18
Java(TM) SE Runtime Environment (build 20.0.1+9-29)
Java HotSpot(TM) 64-Bit Server VM (build 20.0.1+9-29, mixed mode, sharing)
```

Если у вас несколько версий Java одновременно - то хотя бы одна из них должна быть 17+
Если java не установлена вовсе, то рекомендую установить OpenJDK (например, из https://adoptium.net/en-GB/)

#### 8. Установить пакетый менеджер для сборки front-end npm

[Инструкция](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm).
Рекомендованная версия Node.js - 18.13.0 (LTS)

# Запуск Rangiffler локальное в IDE:

#### 1. Запуск rangiffler-gateway (указать profile = local)

```bash
./gradlew :rangiffler-gateway:bootRun --args='--spring.profiles.active=local'
```
#### 2. Запуск rangiffler-auth (указать profile = local)

```bash
./gradlew :rangiffler-auth:bootRun --args='--spring.profiles.active=local'
```

#### 3. Запуск rangiffler-userdata (указать profile = local)

```bash
./gradlew :rangiffler-userdata:bootRun --args='--spring.profiles.active=local'
```

#### 4. Запустить фронтенд (сначала обновить зависимости)

- Перейти в директорию с фронтом 

```bash
cd rangiffler-client
```
- Запуск (Если собирается впервые, то необходимо сбилдить node-modules c помощью npm i) 
```bash
npm i 
npm start
```

## Enjoy the Rangiffler