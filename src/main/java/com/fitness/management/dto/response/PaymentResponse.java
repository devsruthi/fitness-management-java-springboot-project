package com.fitness.management.dto.response;

import com.fitness.management.entity.Payment;
import com.fitness.management.entity.enums.PaymentMethod;
import com.fitness.management.entity.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponse(
        Integer paymentId,
        Integer subscriptionId,
        Integer memberId,
        String planName,
        BigDecimal paymentAmount,
        LocalDate paymentDate,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getSubscription().getSubscriptionId(),
                payment.getSubscription().getMember().getMemberId(),
                payment.getSubscription().getPlan().getPlanName(),
                payment.getPaymentAmount(),
                payment.getPaymentDate(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus());
    }
}
