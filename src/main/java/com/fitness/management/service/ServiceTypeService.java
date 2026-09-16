package com.fitness.management.service;

import com.fitness.management.dto.response.ServiceTypeResponse;
import java.util.List;

public interface ServiceTypeService {

    List<ServiceTypeResponse> listActiveServiceTypes();
}
