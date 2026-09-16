package com.fitness.management.service.impl;

import com.fitness.management.dto.response.ServiceTypeResponse;
import com.fitness.management.entity.enums.ServiceTypeStatus;
import com.fitness.management.repository.ServiceTypeRepository;
import com.fitness.management.service.ServiceTypeService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeServiceImpl(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Override
    public List<ServiceTypeResponse> listActiveServiceTypes() {
        return serviceTypeRepository.findByServiceTypeStatus(ServiceTypeStatus.ACTIVE).stream()
                .map(ServiceTypeResponse::from)
                .toList();
    }
}
