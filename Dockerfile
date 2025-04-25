FROM eclipse-temurin:17.0.2_8-jdk

VOLUME /tmp

RUN adduser --system --no-create-home spring
USER spring

WORKDIR /app

COPY target/*.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
