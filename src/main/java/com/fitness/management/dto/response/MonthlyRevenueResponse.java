package com.fitness.management.dto.response;

import java.math.BigDecimal;

public record MonthlyRevenueResponse(
        int year,
        int month,
        String monthName,
        BigDecimal totalRevenue,
        long paymentCount
) {
}
