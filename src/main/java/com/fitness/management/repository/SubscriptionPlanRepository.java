package com.fitness.management.repository;

import com.fitness.management.entity.SubscriptionPlan;
import com.fitness.management.entity.enums.PlanStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {

    List<SubscriptionPlan> findByPlanStatus(PlanStatus planStatus);

    boolean existsByPlanName(String planName);

    boolean existsByPlanNameAndPlanIdNot(String planName, Integer planId);
}
