package com.fitness.management.dto.request;

import com.fitness.management.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PurchaseRequest(
        @NotNull PaymentMethod paymentMethod
) {
}
