#!/bin/bash
set -e

echo "=== Starting Urban Services Container ==="

# Check if external DB_URL is provided and not pointing to localhost
if [ -z "$DB_URL" ] || [[ "$DB_URL" == *"localhost"* ]] || [[ "$DB_URL" == *"127.0.0.1"* ]]; then
    echo "No external DB_URL provided (or points to localhost). Starting embedded MariaDB server..."
    
    mkdir -p /etc/mysql/conf.d
    cat << 'EOF' > /etc/mysql/conf.d/low-memory.cnf
[mysqld]
performance_schema = OFF
innodb_buffer_pool_size = 32M
innodb_log_buffer_size = 4M
key_buffer_size = 8M
max_connections = 25
query_cache_size = 0
query_cache_type = 0
EOF

    # Start MariaDB service
    service mariadb start || /etc/init.d/mariadb start || true
    
    # Wait for MariaDB to become ready
    for i in $(seq 1 30); do
        if mariadb -e "SELECT 1;" > /dev/null 2>&1 || mysql -e "SELECT 1;" > /dev/null 2>&1; then
            echo "Database engine is ready."
            break
        fi
        echo "Waiting for database engine... ($i/30)"
        sleep 1
    done

    # Create database and grant privileges to root with password 'root'
    mariadb -e "CREATE DATABASE IF NOT EXISTS urban_services_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null || mysql -e "CREATE DATABASE IF NOT EXISTS urban_services_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    mariadb -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root'; FLUSH PRIVILEGES;" 2>/dev/null || mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root'; FLUSH PRIVILEGES;" 2>/dev/null || true
    
    export DB_URL="jdbc:mysql://localhost:3306/urban_services_db?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&createDatabaseIfNotExist=true"
    export DB_USERNAME="root"
    export DB_PASSWORD="root"
    echo "Local database 'urban_services_db' ready."
else
    echo "Using external database: $DB_URL"
fi

PORT_TO_USE=${PORT:-8080}
echo "Starting Spring Boot application on port $PORT_TO_USE..."

exec java -Xms64m -Xmx280m -XX:+UseSerialGC -Dserver.port=$PORT_TO_USE -jar app.jar
