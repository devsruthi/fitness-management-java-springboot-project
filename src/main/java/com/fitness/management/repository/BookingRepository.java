package com.fitness.management.repository;

import com.fitness.management.entity.Booking;
import com.fitness.management.entity.enums.BookingStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    List<Booking> findAllByOrderByBookingCreatedTimeDesc();

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    List<Booking> findByBookingStatusOrderByBookingCancelledTimeDesc(BookingStatus bookingStatus);

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    List<Booking> findByMember_MemberIdOrderByBookingCreatedTimeDesc(Integer memberId);

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    List<Booking> findByMember_MemberIdAndBookingStatusOrderByBookingCreatedTimeDesc(
            Integer memberId, BookingStatus bookingStatus);

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    List<Booking> findByMember_MemberIdAndBookingStatusOrderByBookingCancelledTimeDesc(
            Integer memberId, BookingStatus bookingStatus);

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    Optional<Booking> findByBookingIdAndMember_MemberId(Integer bookingId, Integer memberId);

    @EntityGraph(attributePaths = {"member", "session", "session.serviceType", "session.trainer"})
    Optional<Booking> findTopByMember_MemberIdAndSession_SessionIdOrderByBookingIdDesc(
            Integer memberId, Integer sessionId);

    boolean existsByMember_MemberIdAndSession_SessionIdAndBookingStatus(
            Integer memberId, Integer sessionId, BookingStatus bookingStatus);

    List<Booking> findBySession_SessionId(Integer sessionId);

    void deleteByMember_MemberId(Integer memberId);

    void deleteBySession_SessionId(Integer sessionId);
}
