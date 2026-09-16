package com.fitness.management.repository;

import com.fitness.management.entity.MemberSubscription;
import com.fitness.management.entity.enums.SubscriptionStatus;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberSubscriptionRepository extends JpaRepository<MemberSubscription, Integer> {

    @Query("""
            SELECT s FROM MemberSubscription s
            JOIN FETCH s.member
            JOIN FETCH s.plan
            WHERE s.subscriptionId = :subscriptionId
            """)
    Optional<MemberSubscription> findWithPlanAndMemberBySubscriptionId(
            @Param("subscriptionId") Integer subscriptionId);

    boolean existsByMember_MemberIdAndPlan_PlanIdAndSubscriptionStatusIn(
            Integer memberId, Integer planId, Collection<SubscriptionStatus> statuses);

    boolean existsByMember_MemberIdAndSubscriptionStatus(Integer memberId, SubscriptionStatus status);

    @Query("""
            SELECT s FROM MemberSubscription s
            JOIN FETCH s.member
            JOIN FETCH s.plan
            WHERE s.member.memberId = :memberId
              AND s.subscriptionStatus = :status
            ORDER BY s.startDate DESC, s.subscriptionId DESC
            """)
    List<MemberSubscription> findByMemberIdAndStatusWithDetails(
            @Param("memberId") Integer memberId,
            @Param("status") SubscriptionStatus status);

    @Query("""
            SELECT s FROM MemberSubscription s
            JOIN FETCH s.member
            JOIN FETCH s.plan
            WHERE s.member.memberId = :memberId
            ORDER BY s.subscriptionId DESC
            """)
    List<MemberSubscription> findByMemberIdWithDetails(@Param("memberId") Integer memberId);

    @Query("""
            SELECT s FROM MemberSubscription s
            JOIN FETCH s.member
            JOIN FETCH s.plan
            ORDER BY s.member.memberId ASC, s.subscriptionId DESC
            """)
    List<MemberSubscription> findAllWithMemberAndPlan();

    void deleteByMember_MemberId(Integer memberId);
}
