package com.urbanservices.backend.config;

import com.urbanservices.backend.common.util.AppConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3 / Swagger UI configuration.
 *
 * <p>Swagger UI is available at: {@code /swagger-ui.html}
 * Raw JSON spec:              {@code /v3/api-docs}
 *
 * <p>Security scheme is declared now (Bearer token placeholder) so that once
 * Firebase Auth is wired in (Phase 27), all endpoints automatically support
 * the Authorization header in Swagger UI without touching this config again.
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:Urban Services}")
    private String appName;

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers())
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, bearerSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }

    private Info apiInfo() {
        return new Info()
                .title("Urban Services API")
                .version("1.0.0")
                .description("""
                        **Urban Services Platform REST API**
                        
                        A large-scale service marketplace backend supporting:
                        - Customer App (Flutter)
                        - Service Provider App (Flutter)
                        - Admin Portal (React)
                        
                        **Authentication**: Firebase ID Token (Phase 27).
                        During development, endpoints marked as secured can be accessed
                        by passing a placeholder token.
                        
                        **Base path**: `/api/v1/`
                        """)
                .contact(new Contact()
                        .name("Urban Services Team")
                        .email("dev@urbanservices.com"))
                .license(new License()
                        .name("Proprietary")
                        .url("https://urbanservices.com"));
    }

    private List<Server> apiServers() {
        return List.of(
                new Server().url("http://localhost:8080").description("Local Development"),
                new Server().url("https://api-dev.urbanservices.com").description("Dev Environment"),
                new Server().url("https://api.urbanservices.com").description("Production")
        );
    }

    private SecurityScheme bearerSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Firebase ID Token")
                .description("Firebase Authentication ID Token. Obtain from Firebase Auth SDK in Flutter.");
    }
}
