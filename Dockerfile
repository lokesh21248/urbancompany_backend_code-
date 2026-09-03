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

# Install bash & curl
RUN apt-get update && apt-get install -y --no-install-recommends bash curl && rm -rf /var/lib/apt/lists/*

# Copy executable JAR
COPY --from=build /app/core-service/target/*.jar app.jar

EXPOSE 8080 10000

# Runs single JVM with Serial GC: uses only ~180MB RAM, perfectly suited for Render 512MB free tier
CMD java -Xms64m -Xmx300m -XX:+UseSerialGC -Dserver.port=${PORT:-8080} -jar app.jar
