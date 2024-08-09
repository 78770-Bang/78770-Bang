package com.inn.restaurant.contImpl.dashboard;

import com.inn.restaurant.controller.dashboard.DashboardController;
import com.inn.restaurant.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardControllerImpl implements DashboardController {

    @Autowired
    DashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return null;
    }
}
