#!/bin/bash
# Build all images and run the e2e stack (services + Selenoid + Allure).
#
# NOTE: the e2e test container will not pass until Selenide remote-driver
# (Selenoid) wiring is added to the test code — see docker-compose.test.yml.
set -e

source ./rangiffler-e-2-e-tests/docker.properties

echo '### Java version ###'
java --version

echo '### Stop previous stack ###'
docker compose -f docker-compose.test.yml down

echo '### Build backend images ###'
./gradlew clean build dockerBuild -x test -x :rangiffler-e-2-e-tests:test

echo '### Build frontend image ###'
docker build -t nelakov/rangiffler-client:latest rangiffler-client

echo '### Build e2e image ###'
ARCH=$(uname -m)
if [ "$ARCH" = "arm64" ] || [ "$ARCH" = "aarch64" ]; then
  E2E_BASE=arm64v8/eclipse-temurin:25-jdk
else
  E2E_BASE=eclipse-temurin:25-jdk
fi
docker build --build-arg DOCKER="$E2E_BASE" \
  -t "${IMAGE_NAME}:${VERSION}" -t "${IMAGE_NAME}:latest" \
  -f ./rangiffler-e-2-e-tests/Dockerfile .

docker pull selenoid/vnc_chrome:128.0
docker images | grep -E 'rangiffler|selenoid'

echo '### Start e2e stack ###'
docker compose -f docker-compose.test.yml up -d
docker compose -f docker-compose.test.yml ps
