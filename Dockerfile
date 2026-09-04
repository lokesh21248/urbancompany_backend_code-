# ── Build Stage ─────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy root pom and core-service pom
COPY pom.xml .
COPY core-service/pom.xml core-service/

# Copy core-service source
COPY core-service core-service

# Build the unified Spring Boot service
RUN mvn clean package -pl core-service -am -DskipTests -B

# ── Runtime Stage ───────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre
WORKDIR /app

# Install bash, curl & mariadb-server (provides automatic zero-config DB fallback)
RUN apt-get update && apt-get install -y --no-install-recommends \
    bash \
    curl \
    mariadb-server \
    mariadb-client \
    && rm -rf /var/lib/apt/lists/*

# Copy executable JAR and entrypoint script
COPY --from=build /app/core-service/target/*.jar app.jar
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

EXPOSE 8080 10000

ENTRYPOINT ["/app/docker-entrypoint.sh"]
