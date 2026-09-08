package com.fitness.management.dto.response;

import com.fitness.management.entity.SubscriptionPlan;
import com.fitness.management.entity.enums.PlanStatus;
import java.math.BigDecimal;

public record SubscriptionPlanResponse(
        Integer planId,
        String planName,
        Integer durationInMonths,
        BigDecimal planPrice,
        String planDescription,
        PlanStatus planStatus,
        Boolean groupClassesAccess,
        Boolean personalTrainingAccess,
        Boolean exclusiveServices
) {
    public static SubscriptionPlanResponse from(SubscriptionPlan plan) {
        return new SubscriptionPlanResponse(
                plan.getPlanId(),
                plan.getPlanName(),
                plan.getDurationInMonths(),
                plan.getPlanPrice(),
                plan.getPlanDescription(),
                plan.getPlanStatus(),
                plan.getGroupClassesAccess(),
                plan.getPersonalTrainingAccess(),
                plan.getExclusiveServices());
    }
}
