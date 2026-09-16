package com.fitness.management.repository;

import com.fitness.management.entity.Payment;
import com.fitness.management.entity.enums.PaymentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("""
            SELECT p FROM Payment p
            JOIN FETCH p.subscription s
            JOIN FETCH s.member
            JOIN FETCH s.plan
            WHERE s.member.memberId = :memberId
            ORDER BY p.paymentDate DESC, p.paymentId DESC
            """)
    List<Payment> findByMemberIdWithDetails(@Param("memberId") Integer memberId);

    List<Payment> findByPaymentStatus(PaymentStatus status);

    void deleteBySubscription_Member_MemberId(Integer memberId);
}
