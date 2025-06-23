FROM openjdk:24-jdk-slim

RUN mkdir /app
COPY build/libs/ai-rss-reader.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]