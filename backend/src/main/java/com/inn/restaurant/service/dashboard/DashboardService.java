package com.inn.restaurant.service.dashboard;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DashboardService {

    ResponseEntity<Map<String, Object>> getDashboard();
}
