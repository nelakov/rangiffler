<p align="center">
<img src="https://github.com/elakovnick24/rangiffler/blob/master/utils/Images/download.svg" width="250"/>
</p>

<div align="center">
  <b style="color: #fafafa; font-size: 54px;">Rangiffler</b>
</div>

<div align="center">
  <i style="font-size: 18px;">Still not the Aviasales, already not the Tripadvisor</i>
</div>

<div id="badges" align="center">
 <img src="https://komarev.com/ghpvc/?username=rangiffler&label=REPO+VIEWS&style=for-the-badge&color=brightgreen" alt="Github Badge"/>
</div>

<br>

# Table of contents

<hr>

- [Project Description](#project-description)
- [About the Project](#about-the-project)
- [Technology stack](#technology-stack)
- [Documentation](#documentation)
- [Microservices architecture](#architecture)
- [Ports of microservices](#ports of microservices)
- [Local launch in IDE](#local launch in IDE)
- [Docker launch](#docker launch)
- [Local test launch](#local test launch)
- [Allure report example](#allure report example)

# Technology stack

<hr>

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
- [Feign](https://github.com/OpenFeign/feign)
- [Allure](https://docs.qameta.io/allure/)
- [Selenide](https://selenide.org/)
- [Selenoid & Selenoid-UI](https://aerokube.com/selenoid/latest/)
- [Allure-docker-service](https://github.com/fescobar/allure-docker-service)
- [Java 19](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html)
- [Gradle 7.6](https://docs.gradle.org/7.6/release-notes.html)
- And much more:)

# Project Description

<hr>

<div style="text-align: left;">
<span style="color: #fafafa; font-size: 14px; line-height: 1.5;">

### Welcome to the amazing world of **Rangiffler**!

The travel service for true rangifer enthusiasts!

Why did we name it **Rangiffler**? Oh, it's simple!

We combined the word "rangifer" (which means "reindeer") with our unique set of features just for you!

- With Rangiffler, you can embark on thrilling journeys around the globe, uploading your photos at every destination.
- Each photo becomes a mark on the map of your incredible odyssey.
- From bustling metropolises to cozy countryside nooks - every corner deserves its own reindeer track!
- And don't stop at solo travels! Make Rangiffler even more fun by adding your friends!
- You'll easily browse through their photos, support their adventures, and even send reindeer-style friendship requests!

Who knows, you might even share some reindeer tricks and travel secrets!

### **But that's not all!**

Discover the unique "Reindeer Reflection" feature - a fun way to see the world from a reindeer's perspective! Just
upload a photo and marvel at how it would look if you were a reindeer!

_**Rangifer**_ - the generic name of the scientific name of a reindeer.

_**Rangiffler**_ - the generic name of the generic name!

Thus, **Rangiffler** becomes not only a travel service but also a universal symbol of generality!

We are aimed at satisfying all your needs and desires in travels, whether you are a true reindeer or just an adventure
enthusiast!

With Rangiffler, everyone can become a true explorer of the world! 🦌✨

With love from the Rangiffler!
</div>

# About the Project

<hr>
<div style="text-align: left;">
<span style="color: #fafafa; font-size: 14px; line-height: 1.5;">

The Rangiffler project is a comprehensive application built on a microservices architecture, providing scalability,
flexibility, and efficient operation.

The project includes several modules, each serving specific functions and offering unique experiences to users.

<p align="left">
<img src="https://github.com/elakovnick24/rangiffler/blob/master/utils/Images/rangiffler_schema.png" width="900"/>
</p>

<a name="ports"></a>
### Ports of microservices

| **Service** |          **Port**          |
|:-----------:|:--------------------------:|
|  FRONTEND   |        80 (server)         |
|   GATEWAY   |       8080 (server)        |
|    AUTH     |       9000 (server)        |
|  USERDATA   | 9010 (server), 9011 (grpc) |
|     GEO     | 9020 (server), 9021 (grpc) |
|    PHOTO    | 9030 (server), 9031 (grpc) |

</div>

## Project Modules

<hr>
<div style="text-align: left;">
<span style="color: #fafafa; font-size: 14px; line-height: 2.0;">

### Frontend

The "Frontend" module represents the client-side of the application.

Here, users can enjoy a user-friendly and intuitive interface, enabling them to interact with various functionalities of
the Rangiffler project.

Embark on thrilling journeys around the globe, upload your photos, connect with friends, and savor every moment with
Rangiffler!

### Gateway

The "Gateway" module is a powerful component that distributes all client requests to the corresponding microservices.

It ensures access control by verifying each request for a JWT token to provide security and user authentication.

The "Gateway" serves as the trustworthy guardian of the magical world of Rangiffler!

### Auth Service

The "Auth Service" is responsible for user authentication, storing credentials in the database, and recording Kafka
messages for each successful authorization.

Interaction with the Gateway module is via the HTTP protocol in a RESTful style, ensuring simple and efficient
communication.

### Userdata Service

The "Userdata Service" is a repository for user information, including personal data and friends' lists.

Interaction with the Gateway module is also through the HTTP protocol in a RESTful style, ensuring high performance and
convenient data management.

### Geo Service

The "Geo Service" is a virtual atlas storing information about countries around the world.

Communication with the Gateway module is based on the gRPC protocol, providing efficient data transmission and
high-speed request processing.

### Photo Service

The "Photo Service" is a reliable repository of user photo details.

Interaction with the Gateway module also utilizes the gRPC protocol, ensuring reliability and speedy data processing.

### Testing

<hr>

#### Unit Testing and Allure Report

Rangiffler takes unit testing seriously to ensure code correctness and reliability.

Each service is diligently covered with comprehensive unit tests.

To provide a transparent and insightful view of the test results, Rangiffler leverages the Allure Report.

#### E2E Testing in Docker Containers

All end-to-end (e2e) tests in Rangiffler are seamlessly executed within isolated Docker containers, ensuring consistency
and portability of the testing environment.

Docker containers provide a clean and reproducible environment for running the tests, guaranteeing reliable and accurate
test results.

This approach ensures that the tests run consistently across different environments, avoiding potential configuration
issues and making the testing process more efficient.

- #### Testing Technologies and Tools

Rangiffler's e2e testing harness leverages a powerful set of testing technologies and tools:

- #### Selenide for WEB Testing:

Selenide is used to perform web testing, providing an elegant and concise API for interacting with web elements, making
the tests easy to read and maintain.

- #### JUnit 5 with Custom Annotations and Extensions:

JUnit 5, enriched with custom annotations and extensions, offers a structured and organized testing framework, enabling
us to write clear and expressive test cases.

- #### Mockito and Wiremock:

Mockito allows us to create mock objects for testing, while Wiremock provides a flexible framework for mocking external
APIs, enabling us to simulate various scenarios in our tests.

- #### Testing gRPC and Kafka:

The e2e tests also cover interactions with gRPC and Kafka, ensuring that all communication channels between services are
thoroughly tested for correctness and reliability.

- #### Infrastructure Prepared with Docker Compose

The entire Rangiffler infrastructure is conveniently set up using Docker Compose.

This enables effortless management and deployment of the application and its dependencies.

Docker Compose ensures that the various modules and services work harmoniously together, delivering a robust and unified
user experience.

### Reporting

<hr>

- #### The Power of Allure

The Allure Report is a feature-rich reporting tool that offers valuable information on test results in an intuitive and
visually appealing manner.

With Allure, we can generate detailed, interactive, and user-friendly reports, making it easy to understand and analyze
test outcomes.

- #### Customizations for Enhanced Reporting

Rangiffler's Allure Report is customized to provide even more insights into the test results and to aid in
troubleshooting potential issues:

- #### Detailed Logs for Each Test:

Every test case includes its own log file in HTML format, making it easy to analyze any potential problems that occurred
during the test execution.

- #### Logging Database Queries:

All database queries made during the test execution are logged and attached to the report as separate steps with applied
CSS styles.

This provides a transparent view of database interactions and helps in identifying performance or data-related issues.

#### Simplifying Test Analysis

The Allure Report simplifies the analysis of test results, helping the development team to quickly identify and address
any issues.

With the customized reports, we gain deeper insights into the application's behavior and can take informed actions to
enhance its overall reliability and performance.
</div>

# Documentation
---

<div style="text-align: left;">
<span style="color: #fafafa; font-size: 18px; line-height: 1.5;">

**Minimum Requirements for Working with Rangiffler Project**

To work with the Rangiffler project, you need to ensure the following minimum requirements are met:

1. Install Docker (If not already installed)
   Docker is used for the database (MySQL) and running microservices in a unified Docker network using docker-compose.

    - [Install Docker on Windows](https://docs.docker.com/desktop/install/windows-install/)
    - [Install Docker on Mac](https://docs.docker.com/desktop/install/mac-install/) (Different packages for ARM and
      Intel)
    - [Install Docker on Linux](https://docs.docker.com/desktop/install/linux-install/)

   After installing and starting the Docker daemon, you can verify the Docker commands by running `docker -v`:

    ``` bash
    docker -v
    ```

2. Pull MySQL Container Version 8.0.33

    ```bash
    docker pull mysql:8.0.33
    ```

   After pulling, you will see the downloaded image using the command `docker images`:

    ``` posh
    ❯ docker images
    REPOSITORY                  TAG       IMAGE ID       CREATED         SIZE
    mysql                       8.0.33    a5e6f938c138   9 days ago      587MB
    ```

3. Create a Volume for Data Persistence from the Database in Docker on Your Computer

    ``` bash
    docker volume create mysqldata
    ```

4. Start the Database with the Following Command

    ``` bash
    docker run --name rangiffler-all -p 3306:3306 -e MYSQL_ROOT_PASSWORD=secret -v mysqldata:/var/lib/mysql -d mysql:8.0.33
    ```

5. Install One of the Programs for Visual Interaction with MySQL

   For example, you can use DBeaver or Datagrip.

6. Connect to the MySQL Database (host: localhost, port: 3306, user: root, pass: secret)

7. Install Java Version 17 or Higher

   Rangiffler does not support versions older than 17. To check your installed Java version, run `java -version`:

    ``` posh
    ❯ java -version
    java version "20.0.1" 2023-04-18
    Java(TM) SE Runtime Environment (build 20.0.1+9-29)
    Java HotSpot(TM) 64-Bit Server VM (build 20.0.1+9-29, mixed mode, sharing)
    ```

   If you have multiple Java versions installed, at least one of them should be 17+. If Java is not installed, you can
   install OpenJDK (e.g., from https://adoptium.net/en-GB/).

8. Install the Package Manager for Front-End Build: npm

   Follow the instructions
   to [Install Node.js and npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm). The recommended
   version of Node.js is 18.13.0 (LTS).

</span>
</div>

<hr>

<div style="text-align: left;">
<span style="color: #fafafa; font-size: 18px; line-height: 2.0;">

**Running Rangiffler Locally in IDE:**

1. Start rangiffler-gateway (specify profile = local)

    ```bash
    ./gradlew :rangiffler-gateway:bootRun --args='--spring.profiles.active=local'
    ```

2. Start rangiffler-auth (specify profile = local)

    ```bash
    ./gradlew :rangiffler-auth:bootRun --args='--spring.profiles.active=local'
    ```

3. Start rangiffler-userdata (specify profile = local)

    ```bash
    ./gradlew :rangiffler-userdata:bootRun --args='--spring.profiles.active=local'
    ```

4. Start the Frontend (Make sure to update dependencies first)

    - Navigate to the frontend directory:

    ```bash
    cd rangiffler-client
    ```

    - Start the frontend (If it's the first time, build the node-modules with npm i):

    ```bash
    npm i 
    npm start
    ```

</span>
</div>

<div align="center">
  <b style="color: #fafafa; font-size: 54px;">Enjoy the Rangiffler</b>
</div>

<div align="center">
  <i style="font-size: 18px;">Travel! It's worth it!</i>
</div>

<hr>
<p align="center">
<img src="https://github.com/elakovnick24/rangiffler/blob/master/utils/Images/mimino_rangiffler.jpg" width="800"/>
</p>
