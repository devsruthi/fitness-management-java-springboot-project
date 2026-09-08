package com.fitness.management.dto.request;

import com.fitness.management.entity.enums.PlanStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record SubscriptionPlanRequest(
        @NotBlank @Size(max = 170) String planName,
        @NotNull @Positive Integer durationInMonths,
        @NotNull @PositiveOrZero BigDecimal planPrice,
        @Size(max = 250) String planDescription,
        PlanStatus planStatus,
        Boolean groupClassesAccess,
        Boolean personalTrainingAccess,
        Boolean exclusiveServices
) {
}
