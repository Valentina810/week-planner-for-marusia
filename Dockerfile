FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . /app/.
RUN mvn -f /app/pom.xml clean package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
COPY tokens.json /app
COPY commands.json /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]