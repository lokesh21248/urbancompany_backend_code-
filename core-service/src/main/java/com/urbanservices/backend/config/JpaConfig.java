package com.urbanservices.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA & Database transaction configuration.
 *
 * <ul>
 *   <li>Enables Spring Data JPA auditing for automatic {@code createdAt} and {@code updatedAt} population.</li>
 *   <li>Enables declarative transaction management across services.</li>
 * </ul>
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {
}
