package com.fitness.management.controller;

import com.fitness.management.dto.response.CancelledSessionStatsResponse;
import com.fitness.management.dto.response.MonthlyRevenueResponse;
import com.fitness.management.dto.response.ServiceTypePopularityResponse;
import com.fitness.management.dto.response.YearlyRevenueResponse;
import com.fitness.management.service.DashboardService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/most-booked-service-types")
    public ResponseEntity<List<ServiceTypePopularityResponse>> mostBookedServiceTypes() {
        return ResponseEntity.ok(dashboardService.listMostBookedServiceTypes());
    }

    @GetMapping("/most-cancelled-sessions")
    public ResponseEntity<List<CancelledSessionStatsResponse>> mostCancelledSessions() {
        return ResponseEntity.ok(dashboardService.listMostCancelledSessions());
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<MonthlyRevenueResponse>> monthlyRevenue(
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(dashboardService.listMonthlyRevenue(year));
    }

    @GetMapping("/revenue/yearly")
    public ResponseEntity<List<YearlyRevenueResponse>> yearlyRevenue() {
        return ResponseEntity.ok(dashboardService.listYearlyRevenue());
    }
}
