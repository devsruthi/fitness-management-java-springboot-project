package com.fitness.management.service;

import com.fitness.management.dto.response.CancelledSessionStatsResponse;
import com.fitness.management.dto.response.MonthlyRevenueResponse;
import com.fitness.management.dto.response.ServiceTypePopularityResponse;
import com.fitness.management.dto.response.YearlyRevenueResponse;
import java.util.List;

public interface DashboardService {

    List<ServiceTypePopularityResponse> listMostBookedServiceTypes();

    List<CancelledSessionStatsResponse> listMostCancelledSessions();

    List<MonthlyRevenueResponse> listMonthlyRevenue(Integer year);

    List<YearlyRevenueResponse> listYearlyRevenue();
}
