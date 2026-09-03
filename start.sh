#!/bin/bash
set -e

echo "=== Starting Urban Services Microservices on Render ==="

# Render passes the listening port in $PORT (e.g. 10000)
GATEWAY_PORT=${PORT:-8080}

echo "1. Starting Eureka Service Registry on port 8761..."
java -Xms32m -Xmx64m -XX:+UseSerialGC -Xss256k -jar service-registry.jar > /dev/null 2>&1 &

sleep 5

echo "2. Starting Catalog Service on port 8082..."
java -Xms48m -Xmx96m -XX:+UseSerialGC -Xss256k -jar catalog-service.jar &

echo "3. Starting Core Service on port 8081..."
java -Xms64m -Xmx128m -XX:+UseSerialGC -Xss256k -jar core-service.jar &

sleep 5

echo "4. Starting API Gateway on port $GATEWAY_PORT..."
exec java -Xms48m -Xmx96m -XX:+UseSerialGC -Xss256k -Dserver.port=$GATEWAY_PORT -jar api-gateway.jar
