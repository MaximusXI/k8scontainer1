FROM eclipse-temurin:17.0.11_9-jre-ubi9-minimal

WORKDIR /appentry

COPY target/entry-0.0.1-SNAPSHOT.jar appentry.jar
COPY src/main/resources/application.properties /appentry/config/
EXPOSE 6000
ENTRYPOINT ["java", "-jar", "appentry.jar","--spring.config.location=/appentry/config/application.properties"]