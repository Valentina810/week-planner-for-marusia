FROM jelastic/maven:3.9.5-openjdk-21 as builder
WORKDIR /app
COPY . /app/.
COPY settings.xml /root/.m2/settings.xml
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN
RUN echo "machine github.com login $GITHUB_USERNAME password $GITHUB_TOKEN" > ~/.netrc
RUN mvn -f /app/pom.xml clean package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/*.jar
COPY tokens.json /app
COPY commands.json /app
EXPOSE 443
ENTRYPOINT ["java", "-jar", "/app/*.jar"]