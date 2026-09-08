package com.fitness.management.service.impl;

import com.fitness.management.dto.request.PurchaseRequest;
import com.fitness.management.dto.request.SubscriptionPlanRequest;
import com.fitness.management.dto.request.SubscriptionRequest;
import com.fitness.management.dto.response.MemberSubscriptionResponse;
import com.fitness.management.dto.response.PurchaseResponse;
import com.fitness.management.dto.response.SubscriptionPlanResponse;
import com.fitness.management.entity.MemberSubscription;
import com.fitness.management.entity.SubscriptionPlan;
import com.fitness.management.entity.enums.PlanStatus;
import com.fitness.management.exception.BusinessRuleException;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.MemberSubscriptionRepository;
import com.fitness.management.repository.StoredProcedureRepository;
import com.fitness.management.repository.StoredProcedureRepository.PurchaseProcedureResult;
import com.fitness.management.repository.SubscriptionPlanRepository;
import com.fitness.management.service.SubscriptionService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final StoredProcedureRepository storedProcedureRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final MemberSubscriptionRepository memberSubscriptionRepository;

    public SubscriptionServiceImpl(
            StoredProcedureRepository storedProcedureRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            MemberSubscriptionRepository memberSubscriptionRepository) {
        this.storedProcedureRepository = storedProcedureRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.memberSubscriptionRepository = memberSubscriptionRepository;
    }

    @Override
    public List<SubscriptionPlanResponse> listActivePlans() {
        return subscriptionPlanRepository.findByPlanStatus(PlanStatus.ACTIVE).stream()
                .map(SubscriptionPlanResponse::from)
                .toList();
    }

    @Override
    public MemberSubscriptionResponse addSubscription(Integer memberId, SubscriptionRequest request) {
        Integer subscriptionId = storedProcedureRepository.addSubscription(memberId, request.planId());
        MemberSubscription subscription = memberSubscriptionRepository
                .findWithPlanAndMemberBySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription was created but could not be loaded"));
        return MemberSubscriptionResponse.from(
                subscription,
                "Subscription plan added to cart. Complete payment to activate.");
    }

    @Override
    public PurchaseResponse purchase(Integer subscriptionId, PurchaseRequest request) {
        PurchaseProcedureResult result = storedProcedureRepository.purchaseSubscription(
                subscriptionId, request.paymentMethod().name());
        return new PurchaseResponse(
                result.paymentId(),
                result.subscriptionId() != null ? result.subscriptionId() : subscriptionId,
                result.message());
    }

    @Override
    public SubscriptionPlanResponse getPlan(Integer planId) {
        return SubscriptionPlanResponse.from(findPlan(planId));
    }

    @Override
    @Transactional
    public SubscriptionPlanResponse createPlan(SubscriptionPlanRequest request) {
        if (subscriptionPlanRepository.existsByPlanName(request.planName())) {
            throw new BusinessRuleException("A subscription plan with this name already exists");
        }
        SubscriptionPlan plan = new SubscriptionPlan();
        applyPlanFields(plan, request);
        if (plan.getPlanStatus() == null) {
            plan.setPlanStatus(PlanStatus.ACTIVE);
        }
        return SubscriptionPlanResponse.from(subscriptionPlanRepository.save(plan));
    }

    @Override
    @Transactional
    public SubscriptionPlanResponse updatePlan(Integer planId, SubscriptionPlanRequest request) {
        SubscriptionPlan plan = findPlan(planId);
        if (subscriptionPlanRepository.existsByPlanNameAndPlanIdNot(request.planName(), planId)) {
            throw new BusinessRuleException("A subscription plan with this name already exists");
        }
        applyPlanFields(plan, request);
        return SubscriptionPlanResponse.from(subscriptionPlanRepository.save(plan));
    }

    @Override
    @Transactional
    public void deactivatePlan(Integer planId) {
        SubscriptionPlan plan = findPlan(planId);
        plan.setPlanStatus(PlanStatus.INACTIVE);
        subscriptionPlanRepository.save(plan);
    }

    private SubscriptionPlan findPlan(Integer planId) {
        return subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription plan not found"));
    }

    private void applyPlanFields(SubscriptionPlan plan, SubscriptionPlanRequest request) {
        plan.setPlanName(request.planName());
        plan.setDurationInMonths(request.durationInMonths());
        plan.setPlanPrice(request.planPrice());
        plan.setPlanDescription(request.planDescription());
        plan.setPlanStatus(request.planStatus() != null ? request.planStatus() : PlanStatus.ACTIVE);
        plan.setGroupClassesAccess(request.groupClassesAccess() != null ? request.groupClassesAccess() : Boolean.TRUE);
        plan.setPersonalTrainingAccess(
                request.personalTrainingAccess() != null ? request.personalTrainingAccess() : Boolean.FALSE);
        plan.setExclusiveServices(request.exclusiveServices() != null ? request.exclusiveServices() : Boolean.FALSE);
    }
}
