package com.fitness.management.repository;

import com.fitness.management.entity.MemberSubscription;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSubscriptionRepository extends JpaRepository<MemberSubscription, Integer> {

    @EntityGraph(attributePaths = {"member", "plan"})
    Optional<MemberSubscription> findWithPlanAndMemberBySubscriptionId(Integer subscriptionId);
}
