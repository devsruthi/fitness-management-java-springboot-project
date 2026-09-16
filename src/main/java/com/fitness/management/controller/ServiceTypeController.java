package com.fitness.management.controller;

import com.fitness.management.dto.response.ServiceTypeResponse;
import com.fitness.management.service.ServiceTypeService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-types")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceTypeResponse>> listActiveServiceTypes() {
        return ResponseEntity.ok(serviceTypeService.listActiveServiceTypes());
    }
}
