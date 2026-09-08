package com.fitness.management.service;

import com.fitness.management.dto.request.PurchaseRequest;
import com.fitness.management.dto.request.SubscriptionPlanRequest;
import com.fitness.management.dto.request.SubscriptionRequest;
import com.fitness.management.dto.response.MemberSubscriptionResponse;
import com.fitness.management.dto.response.PurchaseResponse;
import com.fitness.management.dto.response.SubscriptionPlanResponse;
import java.util.List;

public interface SubscriptionService {

    List<SubscriptionPlanResponse> listActivePlans();

    MemberSubscriptionResponse addSubscription(Integer memberId, SubscriptionRequest request);

    PurchaseResponse purchase(Integer subscriptionId, PurchaseRequest request);

    SubscriptionPlanResponse getPlan(Integer planId);

    SubscriptionPlanResponse createPlan(SubscriptionPlanRequest request);

    SubscriptionPlanResponse updatePlan(Integer planId, SubscriptionPlanRequest request);

    void deactivatePlan(Integer planId);
}
