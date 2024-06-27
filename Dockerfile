FROM openjdk:17-jdk-alpine
COPY . /src
COPY gradlew /src
WORKDIR /src
RUN ./gradlew build -x test

FROM openjdk:17-jdk-alpine
COPY --from=0 /src/build/libs/levo-pra-voce-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
