package com.inn.restaurant.serviceImpl.dashboard;

import com.inn.restaurant.service.dashboard.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class DashboardServiceImpl implements DashboardService {
    @Override
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return null;
    }
}
