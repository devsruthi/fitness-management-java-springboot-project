package com.fitness.management.dto.response;

import com.fitness.management.entity.ServiceType;
import com.fitness.management.entity.enums.ServiceMode;
import com.fitness.management.entity.enums.ServiceTypeStatus;

public record ServiceTypeResponse(
        Integer serviceTypeId,
        String serviceTypeName,
        String serviceTypeDescription,
        ServiceMode serviceMode,
        Integer maxParticipants,
        ServiceTypeStatus serviceTypeStatus
) {
    public static ServiceTypeResponse from(ServiceType serviceType) {
        return new ServiceTypeResponse(
                serviceType.getServiceTypeId(),
                serviceType.getServiceTypeName(),
                serviceType.getServiceTypeDescription(),
                serviceType.getServiceMode(),
                serviceType.getMaxParticipants(),
                serviceType.getServiceTypeStatus());
    }
}
