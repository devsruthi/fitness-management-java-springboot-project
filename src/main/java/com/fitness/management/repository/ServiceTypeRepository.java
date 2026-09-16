package com.fitness.management.repository;

import com.fitness.management.entity.ServiceType;
import com.fitness.management.entity.enums.ServiceTypeStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {

    List<ServiceType> findByServiceTypeStatus(ServiceTypeStatus serviceTypeStatus);
}
