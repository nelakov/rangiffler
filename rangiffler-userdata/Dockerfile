ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY build/libs/rangiffler-userdata-$VER.jar ./

EXPOSE 9030 9031
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rangiffler-userdata-$VER.jar
