ARG DOCKER
FROM $DOCKER
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY build/libs/rangiffler-country-$VER.jar ./

EXPOSE 9011
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rangiffler-country-$VER.jar
