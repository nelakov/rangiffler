#!/bin/bash
# Build all images and start the full Rangiffler stack.
set -e

echo '### Java version ###'
java --version

echo '### Stop previous stack ###'
docker compose down

echo '### Build backend images ###'
./gradlew clean build dockerBuild -x test -x :rangiffler-e-2-e-tests:test

echo '### Build frontend image ###'
docker build -t nelakov/rangiffler-client:latest rangiffler-client

docker images | grep rangiffler

echo '### Start stack ###'
docker compose up -d
docker compose ps
