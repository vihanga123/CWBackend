# ---------- Build Stage ----------
FROM maven:3.8.6-eclipse-temurin-8-alpine AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build application
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- Runtime Stage ----------
FROM eclipse-temurin:8-jre-alpine

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy the fat JAR
COPY --from=build /app/target/*.jar app.jar

# Fix ownership
RUN chown spring:spring app.jar
USER spring

EXPOSE 8444

# Proper health check for Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=3s --start-period=45s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]