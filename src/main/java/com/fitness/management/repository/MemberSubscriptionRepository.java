package com.fitness.management.repository;

import com.fitness.management.entity.MemberSubscription;
import com.fitness.management.entity.enums.SubscriptionStatus;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSubscriptionRepository extends JpaRepository<MemberSubscription, Integer> {

    @EntityGraph(attributePaths = {"member", "plan"})
    Optional<MemberSubscription> findWithPlanAndMemberBySubscriptionId(Integer subscriptionId);

    boolean existsByMember_MemberIdAndPlan_PlanIdAndSubscriptionStatusIn(
            Integer memberId, Integer planId, Collection<SubscriptionStatus> statuses);

    boolean existsByMember_MemberIdAndSubscriptionStatus(Integer memberId, SubscriptionStatus status);
}
