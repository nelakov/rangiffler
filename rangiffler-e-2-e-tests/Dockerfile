ARG DOCKER
FROM $DOCKER
ENV JAVA_OPTS=""

WORKDIR /rangiffler
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY ./rangiffler-e-2-e-tests ./rangiffler-e-2-e-tests
COPY ./rangiffler-grpc-common ./rangiffler-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./

# Allure results land in build/allure-results — shared with the
# allure-docker-service container via a compose volume.
CMD ./gradlew :rangiffler-e-2-e-tests:test -Denv=docker
