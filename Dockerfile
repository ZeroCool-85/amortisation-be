FROM openjdk:11
WORKDIR /data

COPY target target
ARG JAR_FILE=target/amortisationschedule-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} amortisationschedule.jar

EXPOSE 8081

ENTRYPOINT ["java","-Xmx256m", "-Xms256m", "-jar","amortisationschedule.jar"]