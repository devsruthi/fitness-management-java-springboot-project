package com.fitness.management.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookingRequest(
        @NotNull @Positive Integer sessionId
) {
}
