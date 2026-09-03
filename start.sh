#!/bin/bash
set -e

echo "=== Starting Urban Services Microservices on Render ==="

# Render passes the listening port in $PORT (e.g. 10000)
GATEWAY_PORT=${PORT:-8080}

echo "1. Starting Eureka Service Registry on port 8761..."
java -Xmx128m -jar service-registry.jar &

sleep 6

echo "2. Starting Catalog Service on port 8082..."
java -Xmx192m -jar catalog-service.jar &

echo "3. Starting Core Service on port 8081..."
java -Xmx256m -jar core-service.jar &

sleep 8

echo "4. Starting API Gateway on port $GATEWAY_PORT..."
exec java -Xmx192m -Dserver.port=$GATEWAY_PORT -jar api-gateway.jar
