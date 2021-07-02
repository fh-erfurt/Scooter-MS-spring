FROM maven:3.8-adoptopenjdk-15

COPY . /

RUN mvn package -f pom.xml

ENTRYPOINT ["java","-jar","/target/scooterms-spring-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]

EXPOSE 8707

# Metadata
ARG IMAGE_VERSION=unknown
LABEL \
      org.label-schema.name="scooterms-spring-backend" \
      org.label-schema.description="Spring Application" \
      org.label-schema.version="${IMAGE_VERSION}" \
      org.label-schema.vcs-url="https://github.com/fh-erfurt/Scooter-MS-spring" \
      org.label-schema.schema-version="1.0"
