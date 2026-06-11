ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY build/libs/rangiffler-photo-$VER.jar ./

EXPOSE 9020 9021
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rangiffler-photo-$VER.jar
