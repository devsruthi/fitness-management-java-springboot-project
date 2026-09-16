package com.fitness.management.service.impl;

import com.fitness.management.dto.request.PurchaseRequest;
import com.fitness.management.dto.request.SubscriptionPlanRequest;
import com.fitness.management.dto.request.SubscriptionRequest;
import com.fitness.management.dto.response.MemberSubscriptionResponse;
import com.fitness.management.dto.response.MemberWithSubscriptionResponse;
import com.fitness.management.dto.response.PaymentResponse;
import com.fitness.management.dto.response.PurchaseResponse;
import com.fitness.management.dto.response.SubscriptionPlanResponse;
import com.fitness.management.entity.Member;
import com.fitness.management.entity.MemberSubscription;
import com.fitness.management.entity.Payment;
import com.fitness.management.entity.SubscriptionPlan;
import com.fitness.management.entity.enums.AccountStatus;
import com.fitness.management.entity.enums.PaymentStatus;
import com.fitness.management.entity.enums.PlanStatus;
import com.fitness.management.entity.enums.SubscriptionStatus;
import com.fitness.management.exception.BusinessRuleException;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.MemberRepository;
import com.fitness.management.repository.MemberSubscriptionRepository;
import com.fitness.management.repository.PaymentRepository;
import com.fitness.management.repository.SubscriptionPlanRepository;
import com.fitness.management.service.SubscriptionService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final MemberSubscriptionRepository memberSubscriptionRepository;
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;

    public SubscriptionServiceImpl(
            SubscriptionPlanRepository subscriptionPlanRepository,
            MemberSubscriptionRepository memberSubscriptionRepository,
            MemberRepository memberRepository,
            PaymentRepository paymentRepository) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.memberSubscriptionRepository = memberSubscriptionRepository;
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<SubscriptionPlanResponse> listActivePlans() {
        return subscriptionPlanRepository.findByPlanStatus(PlanStatus.ACTIVE).stream()
                .map(SubscriptionPlanResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public MemberSubscriptionResponse addSubscription(Integer memberId, SubscriptionRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessRuleException("Member not found or not active!"));
        if (member.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BusinessRuleException("Member not found or not active!");
        }

        SubscriptionPlan plan = subscriptionPlanRepository.findById(request.planId())
                .orElseThrow(() -> new BusinessRuleException("Subscription plan not found or not active!"));
        if (plan.getPlanStatus() != PlanStatus.ACTIVE) {
            throw new BusinessRuleException("Subscription plan not found or not active!");
        }

        boolean alreadySelected = memberSubscriptionRepository
                .existsByMember_MemberIdAndPlan_PlanIdAndSubscriptionStatusIn(
                        memberId,
                        request.planId(),
                        List.of(SubscriptionStatus.PENDING, SubscriptionStatus.ACTIVE));
        if (alreadySelected) {
            throw new BusinessRuleException(
                    "You have already chosen this subscription plan and it is pending/active!");
        }

        MemberSubscription subscription = new MemberSubscription();
        subscription.setMember(member);
        subscription.setPlan(plan);
        subscription.setStartDate(LocalDate.now());
        subscription.setSubscriptionStatus(SubscriptionStatus.PENDING);

        MemberSubscription saved = memberSubscriptionRepository.save(subscription);
        return MemberSubscriptionResponse.from(
                saved,
                "Subscription plan added to cart. Complete payment to activate.");
    }

    @Override
    @Transactional(readOnly = true)
    public MemberSubscriptionResponse getCurrentOrLatestSubscription(Integer memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found");
        }

        return memberSubscriptionRepository
                .findByMemberIdAndStatusWithDetails(memberId, SubscriptionStatus.ACTIVE)
                .stream()
                .findFirst()
                .map(subscription -> MemberSubscriptionResponse.from(subscription, "Current active subscription"))
                .orElseGet(() -> MemberSubscriptionResponse.from(
                        memberSubscriptionRepository
                                .findByMemberIdWithDetails(memberId)
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        "No subscription found for this member")),
                        "Latest subscription"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> listMemberPayments(Integer memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found");
        }
        return paymentRepository
                .findByMemberIdWithDetails(memberId)
                .stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberWithSubscriptionResponse> listMembersWithActiveSubscription() {
        LocalDate today = LocalDate.now();
        return latestSubscriptions().stream()
                .filter(subscription -> isCurrentlyActive(subscription, today))
                .map(MemberWithSubscriptionResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberWithSubscriptionResponse> listMembersExpiringSoon(Integer days) {
        if (days == null || days < 1) {
            throw new BusinessRuleException("days must be 1 or more", HttpStatus.BAD_REQUEST);
        }

        LocalDate today = LocalDate.now();
        LocalDate expiryLimit = today.plusDays(days);
        return latestSubscriptions().stream()
                .filter(subscription -> isCurrentlyActive(subscription, today))
                .filter(subscription -> {
                    LocalDate endDate = endDate(subscription);
                    return !endDate.isAfter(expiryLimit);
                })
                .map(MemberWithSubscriptionResponse::from)
                .toList();
    }

    private List<MemberSubscription> latestSubscriptions() {
        Map<Integer, MemberSubscription> latestByMember = new LinkedHashMap<>();
        for (MemberSubscription subscription : memberSubscriptionRepository.findAllWithMemberAndPlan()) {
            latestByMember.putIfAbsent(subscription.getMember().getMemberId(), subscription);
        }
        return new ArrayList<>(latestByMember.values());
    }

    private boolean isCurrentlyActive(MemberSubscription subscription, LocalDate today) {
        return subscription.getSubscriptionStatus() == SubscriptionStatus.ACTIVE
                && endDate(subscription).isAfter(today);
    }

    private LocalDate endDate(MemberSubscription subscription) {
        return subscription.getStartDate().plusMonths(subscription.getPlan().getDurationInMonths());
    }

    @Override
    @Transactional
    public PurchaseResponse purchase(Integer subscriptionId, PurchaseRequest request) {
        MemberSubscription subscription = memberSubscriptionRepository
                .findWithPlanAndMemberBySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        if (subscription.getSubscriptionStatus() == SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleException("Subscription is already active.");
        }
        if (subscription.getSubscriptionStatus() != SubscriptionStatus.PENDING) {
            throw new BusinessRuleException("You have not chosen the subscription plan!");
        }

        Payment payment = new Payment();
        payment.setSubscription(subscription);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentAmount(subscription.getPlan().getPlanPrice());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        Payment savedPayment = paymentRepository.save(payment);

        subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartDate(LocalDate.now());
        memberSubscriptionRepository.save(subscription);

        return new PurchaseResponse(
                savedPayment.getPaymentId(),
                subscription.getSubscriptionId(),
                "Congrats,Purchase successful, your subscription is active now.");
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
