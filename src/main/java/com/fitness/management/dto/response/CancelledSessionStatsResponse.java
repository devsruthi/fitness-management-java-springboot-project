package com.fitness.management.dto.response;

public record CancelledSessionStatsResponse(
        SessionResponse session,
        long cancelledBookingCount
) {
}
