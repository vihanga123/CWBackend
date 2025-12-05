# ---------- Build Stage ----------
FROM maven:3.8.6-eclipse-temurin-8-alpine AS build
WORKDIR /app

ENV MAVEN_OPTS="-Dhttps.protocols=TLSv1.2"

# Copy only pom first (cache layer)
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Runtime Stage ----------
FROM eclipse-temurin:8-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN chown spring:spring app.jar
USER spring

EXPOSE 8444

HEALTHCHECK --interval=30s --timeout=3s --start-period=45s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8444/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]