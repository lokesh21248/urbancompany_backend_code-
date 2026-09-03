package com.urbanservices.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * Application context load test.
 *
 * <p>Verifies the Spring application context loads without errors.
 * Uses {@code test} profile which requires a real MySQL instance
 * via Testcontainers (configured in Phase 4+ tests).
 *
 * <p>For Phase 1, this test is skipped if no datasource is configured
 * by using a conditional approach. Full integration tests come in Phase 4.
 */
@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(properties = {
        // Disable Flyway during unit tests to avoid DB dependency
        "spring.flyway.enabled=false",
        // Use H2 in-memory for this basic context load test
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        // Disable Redis for context load test
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration"
})
class UrbanServicesApplicationTests {

    @Test
    void contextLoads() {
        // Application context loads without errors
    }
}
