package com.urbanservices.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.urbanservices")
@EntityScan(basePackages = "com.urbanservices")
@EnableJpaRepositories(basePackages = "com.urbanservices")
@ConfigurationPropertiesScan
@EnableAsync
@EnableDiscoveryClient(autoRegister = false)
@EnableFeignClients
public class UrbanServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrbanServicesApplication.class, args);
    }
}
