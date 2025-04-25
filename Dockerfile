FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

RUN adduser -S spring
USER spring

WORKDIR /app

COPY target/*.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar