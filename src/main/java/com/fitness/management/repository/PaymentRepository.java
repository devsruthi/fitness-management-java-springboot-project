package com.fitness.management.repository;

import com.fitness.management.entity.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @EntityGraph(attributePaths = {"subscription", "subscription.member", "subscription.plan"})
    List<Payment> findBySubscription_Member_MemberIdOrderByPaymentDateDescPaymentIdDesc(Integer memberId);

    void deleteBySubscription_Member_MemberId(Integer memberId);
}
