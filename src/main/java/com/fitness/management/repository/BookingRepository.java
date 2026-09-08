package com.fitness.management.repository;

import com.fitness.management.entity.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    List<Booking> findByMember_MemberIdOrderByBookingCreatedTimeDesc(Integer memberId);

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    Optional<Booking> findTopByMember_MemberIdAndSession_SessionIdOrderByBookingIdDesc(
            Integer memberId, Integer sessionId);
}
