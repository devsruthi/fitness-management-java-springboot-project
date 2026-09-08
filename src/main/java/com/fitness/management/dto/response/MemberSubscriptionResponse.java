package com.fitness.management.dto.response;

import com.fitness.management.entity.MemberSubscription;
import com.fitness.management.entity.enums.SubscriptionStatus;
import java.time.LocalDate;

public record MemberSubscriptionResponse(
        Integer subscriptionId,
        Integer memberId,
        Integer planId,
        String planName,
        SubscriptionStatus subscriptionStatus,
        LocalDate startDate,
        String message
) {
    public static MemberSubscriptionResponse from(MemberSubscription subscription, String message) {
        return new MemberSubscriptionResponse(
                subscription.getSubscriptionId(),
                subscription.getMember().getMemberId(),
                subscription.getPlan().getPlanId(),
                subscription.getPlan().getPlanName(),
                subscription.getSubscriptionStatus(),
                subscription.getStartDate(),
                message);
    }
}
