package com.fitness.management.controller;

import com.fitness.management.dto.request.SubscriptionPlanRequest;
import com.fitness.management.dto.response.SubscriptionPlanResponse;
import com.fitness.management.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/subscription-plans")
public class AdminSubscriptionPlanController {

    private final SubscriptionService subscriptionService;

    public AdminSubscriptionPlanController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/{planId}")
    public ResponseEntity<SubscriptionPlanResponse> getPlan(@PathVariable Integer planId) {
        return ResponseEntity.ok(subscriptionService.getPlan(planId));
    }

    @PostMapping
    public ResponseEntity<SubscriptionPlanResponse> createPlan(@Valid @RequestBody SubscriptionPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.createPlan(request));
    }

    @PutMapping("/{planId}")
    public ResponseEntity<SubscriptionPlanResponse> updatePlan(
            @PathVariable Integer planId,
            @Valid @RequestBody SubscriptionPlanRequest request) {
        return ResponseEntity.ok(subscriptionService.updatePlan(planId, request));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deactivatePlan(@PathVariable Integer planId) {
        subscriptionService.deactivatePlan(planId);
        return ResponseEntity.noContent().build();
    }
}
