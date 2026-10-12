package com.example.ems.controller;

import com.example.ems.dto.EmployeeDtos.DashboardResponse;
import com.example.ems.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboard;
    public DashboardController(DashboardService dashboard) { this.dashboard = dashboard; }
    @GetMapping("/summary") public DashboardResponse summary(@RequestHeader(value = "X-Auth-Token", required = false) String token) { return dashboard.summary(token); }
}
