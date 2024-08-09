package com.inn.restaurant.controller.dashboard;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/dashboard")
public interface DashboardController {

    @GetMapping(path = "/Dashboard")
    ResponseEntity<Map<String, Object>>getDashboard();
}
