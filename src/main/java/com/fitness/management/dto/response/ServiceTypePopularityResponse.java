package com.fitness.management.dto.response;

import com.fitness.management.entity.enums.ServiceMode;
import com.fitness.management.entity.enums.ServiceTypeStatus;

public record ServiceTypePopularityResponse(
        Integer serviceTypeId,
        String serviceTypeName,
        String serviceTypeDescription,
        ServiceMode serviceMode,
        long bookingCount,
        Integer maxParticipants,
        ServiceTypeStatus serviceTypeStatus
) {
}
