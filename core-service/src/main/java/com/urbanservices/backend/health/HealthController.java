package com.urbanservices.backend.health;

import com.urbanservices.backend.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Health and metadata endpoint.
 *
 * <p>Used by:
 * <ul>
 *   <li>AWS ECS health checks</li>
 *   <li>ALB target group health checks</li>
 *   <li>Development — quick sanity check that the server is up</li>
 * </ul>
 *
 * <p>Does NOT require authentication.
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "Platform health and version information")
public class HealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Returns platform status. No authentication required.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("status", "UP");
        info.put("service", "Urban Services Backend");
        info.put("version", "1.0.0");
        info.put("timestamp", Instant.now().toString());
        info.put("phase", "Phase 1 — Project Architecture");

        return ResponseEntity.ok(ApiResponse.success("Platform is operational", info));
    }
}
