package com.urbanservices.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Urban Services Platform — Main Application Entry Point
 *
 * <p>Spring Boot 3.x application targeting Java 21.
 * All modules (customer, provider, admin, booking, etc.) are loaded
 * as Spring components within this single deployable JAR.
 *
 * <p>For production deployment on AWS ECS/Fargate, this application must be
 * stateless — no in-memory session state, no local file storage.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients
public class UrbanServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanServicesApplication.class, args);
    }
}
