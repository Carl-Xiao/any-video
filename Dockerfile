FROM openjdk:8-jdk-alpine
COPY ./target/any-video-1.0-SNAPSHOT.jar /home/run/
WORKDIR /home/run/
EXPOSE 8080
CMD ["java", "-jar", "any-video-1.0-SNAPSHOT.jar"]

