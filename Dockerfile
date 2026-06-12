# Use an official Gradle image to build the application
FROM gradle:8.10.1-jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle clean bootJar

# Use an OpenJDK image to run the application
FROM eclipse-temurin:17-jdk
WORKDIR /opt/app
COPY --from=build /app/build/libs/PetPal-0.0.1-SNAPSHOT.jar ./PetPal.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar PetPal.jar"]

