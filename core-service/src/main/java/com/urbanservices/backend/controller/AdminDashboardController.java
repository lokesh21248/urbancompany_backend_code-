package com.urbanservices.backend.controller;

import com.urbanservices.backend.dto.AppConfigDTO;
import com.urbanservices.backend.dto.DashboardStatsDTO;
import com.urbanservices.backend.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(adminDashboardService.getDashboardStats());
    }

    @GetMapping("/config")
    public ResponseEntity<List<AppConfigDTO>> getAllConfigs() {
        return ResponseEntity.ok(adminDashboardService.getAllConfigs());
    }

    @PutMapping("/config/{key}")
    public ResponseEntity<AppConfigDTO> updateConfig(
            @PathVariable String key,
            @RequestBody Map<String, String> body) {
        String value = body.getOrDefault("value", "");
        return ResponseEntity.ok(adminDashboardService.updateConfig(key, value));
    }
}
