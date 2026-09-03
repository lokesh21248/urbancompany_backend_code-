package com.urbanservices.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for Urban Services.
 *
 * <p>Phase 1-5: All API endpoints are open (no authentication required).
 * Firebase JWT authentication will be added in Phase 27.
 *
 * <p>CORS is handled by {@link AppConfig#corsFilter()}.
 * CSRF is disabled because we're a stateless REST API.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> {})  // CORS handled by CorsFilter bean in AppConfig
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // Open all APIs for Phase 1-5 dev
            );
        return http.build();
    }
}
