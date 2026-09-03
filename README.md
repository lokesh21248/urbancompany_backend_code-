# Urban Services — Backend Microservices

Production-ready Spring Boot microservices backend for Urban Services platform.

## Architecture
- **API Gateway** (`api-gateway`): Port `8080` — Central entry point, routes traffic to microservices.
- **Service Registry** (`service-registry`): Port `8761` — Netflix Eureka discovery server.
- **Catalog Service** (`catalog-service`): Port `8082` — Manages categories, subcategories, and services catalog.
- **Core Service** (`core-service`): Port `8081` — Manages bookings, providers, customers, file uploads, and dashboard.

---

## Deploy to Render (Step-by-Step)

### 1. Create a MySQL Database on Render (or Aiven / PlanetScale)
1. In [Render Dashboard](https://dashboard.render.com), create a new **MySQL** database (or use a free cloud MySQL like Aiven / Clever Cloud).
2. Note your database connection details:
   - **Database Host / URL** (e.g. `jdbc:mysql://<host>:<port>/<dbname>?useSSL=true`)
   - **Username**
   - **Password**

### 2. Deploy this Repository on Render
1. In Render Dashboard, click **New +** -> **Web Service**.
2. Connect your GitHub repository:
   `https://github.com/lokesh21248/urbancompany_backend_code-`
3. Configure settings:
   - **Name**: `urban-services-backend`
   - **Runtime**: `Docker`
   - **Dockerfile Path**: `./Dockerfile`
   - **Instance Type**: `Free`
4. Under **Environment Variables**, add:
   - `DB_URL`: your MySQL JDBC URL
   - `DB_USERNAME`: your database username
   - `DB_PASSWORD`: your database password
5. Click **Create Web Service**. Render will automatically build the Docker image and deploy!

---

## Local Development

### Build All Modules
```bash
mvn clean package -DskipTests
```

### Run Services
1. Start MySQL locally on port 3306.
2. Start Service Registry:
   ```bash
   cd service-registry && ..\mvnw.cmd spring-boot:run
   ```
3. Start Catalog Service:
   ```bash
   cd catalog-service && ..\mvnw.cmd spring-boot:run
   ```
4. Start Core Service:
   ```bash
   cd core-service && ..\mvnw.cmd spring-boot:run
   ```
5. Start API Gateway:
   ```bash
   cd api-gateway && ..\mvnw.cmd spring-boot:run
   ```
