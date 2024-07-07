#FROM eclipse-temurin:17.0.11_9-jre-ubi9-minimal
FROM maven:3.8.5-openjdk-17-slim
RUN apt-get update && apt-get install -y \
    vim \
    bash-completion \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /appentry

COPY target/entry-0.0.1-SNAPSHOT.jar appentry.jar
COPY src/main/resources/application.properties /appentry/config/
EXPOSE 6000
ENTRYPOINT ["java", "-jar", "appentry.jar","--spring.config.location=/appentry/config/application.properties"]