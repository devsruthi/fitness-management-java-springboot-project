package com.fitness.management.dto.response;

import com.fitness.management.entity.MemberSubscription;
import com.fitness.management.entity.enums.SubscriptionStatus;
import java.time.LocalDate;

public record MemberWithSubscriptionResponse(
        MemberResponse member,
        Integer subscriptionId,
        String planName,
        Integer durationInMonths,
        SubscriptionStatus subscriptionStatus,
        LocalDate startDate,
        LocalDate endDate
) {
    public static MemberWithSubscriptionResponse from(MemberSubscription subscription) {
        LocalDate endDate = subscription.getStartDate()
                .plusMonths(subscription.getPlan().getDurationInMonths());
        return new MemberWithSubscriptionResponse(
                MemberResponse.from(subscription.getMember()),
                subscription.getSubscriptionId(),
                subscription.getPlan().getPlanName(),
                subscription.getPlan().getDurationInMonths(),
                subscription.getSubscriptionStatus(),
                subscription.getStartDate(),
                endDate);
    }
}
