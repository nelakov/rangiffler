#!/bin/bash

echo '### Java version ###'
java --version
echo '### Gradle version ###'
gradle --version

docker-compose down
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker rmi -f $(docker images | grep 'rangiffler')

if [[ $1 = "gql" ]]; then a="$c"; else a="$d"; fi

var front
if [[ "$1" = "gql" ]]; then front="./rangiffler-frontend-gql/"; else front="./rangiffler-client/"; fi

if [ "$1" = "push" ] || [ "$2" = "push" ]; then
  echo "### Build & push images (front: $front) ###"
  bash ./gradlew clean build dockerPush -x :rangiffler-e-2-e-tests:test
  cd "$front" || exit
  bash ./docker-build.sh dev push
else
  echo "### Build images (front: $front) ###"
  bash ./gradlew clean build dockerTagLatest -x :rangiffler-e-2-e-tests:test
  cd "$front" || exit
  bash ./docker-build.sh dev
fi

cd ../
docker images
docker-compose up -d
docker ps -a
