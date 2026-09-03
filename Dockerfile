# ── Build Stage ─────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy root pom and module poms first for dependency caching
COPY pom.xml .
COPY service-registry/pom.xml service-registry/
COPY api-gateway/pom.xml api-gateway/
COPY catalog-service/pom.xml catalog-service/
COPY core-service/pom.xml core-service/

# Copy all source code
COPY service-registry service-registry
COPY api-gateway api-gateway
COPY catalog-service catalog-service
COPY core-service core-service

# Build all modules into executable JARs
RUN mvn clean package -DskipTests -B

# ── Runtime Stage ───────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre
WORKDIR /app

# Install bash and curl for healthchecks
RUN apt-get update && apt-get install -y --no-install-recommends bash curl && rm -rf /var/lib/apt/lists/*

# Copy built JARs
COPY --from=build /app/service-registry/target/*.jar service-registry.jar
COPY --from=build /app/catalog-service/target/*.jar catalog-service.jar
COPY --from=build /app/core-service/target/*.jar core-service.jar
COPY --from=build /app/api-gateway/target/*.jar api-gateway.jar

COPY start.sh .
RUN chmod +x start.sh

EXPOSE 8080 8081 8082 8761 10000

CMD ["./start.sh"]
