package com.fitness.management.controller;

import com.fitness.management.dto.request.PurchaseRequest;
import com.fitness.management.dto.request.SubscriptionRequest;
import com.fitness.management.dto.response.MemberSubscriptionResponse;
import com.fitness.management.dto.response.PurchaseResponse;
import com.fitness.management.dto.response.SubscriptionPlanResponse;
import com.fitness.management.service.SubscriptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscription-plans")
    public ResponseEntity<List<SubscriptionPlanResponse>> listActivePlans() {
        return ResponseEntity.ok(subscriptionService.listActivePlans());
    }

    @PostMapping("/members/subscriptions")
    public ResponseEntity<MemberSubscriptionResponse> addSubscription(
            @RequestParam Integer memberId,
            @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.addSubscription(memberId, request));
    }

    @PostMapping("/subscriptions/purchase")
    public ResponseEntity<PurchaseResponse> purchase(
            @RequestParam Integer subscriptionId,
            @Valid @RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(subscriptionService.purchase(subscriptionId, request));
    }
}
