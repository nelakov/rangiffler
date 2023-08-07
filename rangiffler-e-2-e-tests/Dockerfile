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

CMD chmod +x ./rangiffler-e-2-e-tests/send_results.sh; ./gradlew test -Dtest.env=docker; bash ./rangiffler-e-2-e-tests/send_results.sh
