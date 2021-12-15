FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /tmp
ARG JAR_FILE
COPY target/noteApp.jar noteApp.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/noteApp.jar"]