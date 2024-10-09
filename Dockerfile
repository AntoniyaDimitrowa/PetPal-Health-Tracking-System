FROM gradle:8.10.1-jdk17
WORKDIR /opt/app
COPY ./build/libs/PetPal-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar PetPal-0.0.1-SNAPSHOT.jar"]