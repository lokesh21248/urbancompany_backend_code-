package com.urbanservices.backend.service;

import com.urbanservices.backend.dto.AppConfigDTO;
import com.urbanservices.backend.dto.DashboardStatsDTO;

import java.util.List;

public interface AdminDashboardService {
    DashboardStatsDTO getDashboardStats();
    List<AppConfigDTO> getAllConfigs();
    AppConfigDTO updateConfig(String key, String value);
}
