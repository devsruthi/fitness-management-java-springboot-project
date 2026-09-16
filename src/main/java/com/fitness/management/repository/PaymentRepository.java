package com.fitness.management.repository;

import com.fitness.management.entity.Payment;
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

    @Query(value = """
            SELECT YEAR(payment_date) AS revenue_year,
                   MONTH(payment_date) AS revenue_month,
                   SUM(payment_amount) AS total_revenue,
                   COUNT(*) AS payment_count
            FROM Payments
            WHERE payment_status = 'SUCCESS'
            GROUP BY YEAR(payment_date), MONTH(payment_date)
            ORDER BY revenue_year, revenue_month
            """, nativeQuery = true)
    List<Object[]> sumSuccessfulPaymentsByMonth();

    @Query(value = """
            SELECT YEAR(payment_date) AS revenue_year,
                   SUM(payment_amount) AS total_revenue,
                   COUNT(*) AS payment_count
            FROM Payments
            WHERE payment_status = 'SUCCESS'
            GROUP BY YEAR(payment_date)
            ORDER BY revenue_year
            """, nativeQuery = true)
    List<Object[]> sumSuccessfulPaymentsByYear();

    void deleteBySubscription_Member_MemberId(Integer memberId);
}
