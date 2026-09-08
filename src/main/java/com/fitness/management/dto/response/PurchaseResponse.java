package com.fitness.management.dto.response;

public record PurchaseResponse(
        Integer paymentId,
        Integer subscriptionId,
        String message
) {
}
