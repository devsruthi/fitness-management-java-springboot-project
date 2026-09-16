package com.fitness.management.dto.response;

import java.math.BigDecimal;

public record YearlyRevenueResponse(
        int year,
        BigDecimal totalRevenue,
        long paymentCount
) {
}
