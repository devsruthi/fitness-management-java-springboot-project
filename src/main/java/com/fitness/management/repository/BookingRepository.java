package com.fitness.management.repository;

import com.fitness.management.entity.Booking;
import com.fitness.management.entity.enums.BookingStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.member
            JOIN FETCH b.session s
            JOIN FETCH s.serviceType
            JOIN FETCH s.trainer
            ORDER BY b.bookingCreatedTime DESC
            """)
    List<Booking> findAllWithDetails();

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.member
            JOIN FETCH b.session s
            JOIN FETCH s.serviceType
            JOIN FETCH s.trainer
            WHERE b.bookingStatus = :status
            ORDER BY b.bookingCancelledTime DESC
            """)
    List<Booking> findByStatusWithDetails(@Param("status") BookingStatus status);

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.member
            JOIN FETCH b.session s
            JOIN FETCH s.serviceType
            JOIN FETCH s.trainer
            WHERE b.member.memberId = :memberId
            ORDER BY b.bookingCreatedTime DESC
            """)
    List<Booking> findByMemberIdWithDetails(@Param("memberId") Integer memberId);

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.member
            JOIN FETCH b.session s
            JOIN FETCH s.serviceType
            JOIN FETCH s.trainer
            WHERE b.member.memberId = :memberId
              AND b.bookingStatus = :status
            ORDER BY b.bookingCreatedTime DESC
            """)
    List<Booking> findByMemberIdAndStatusWithDetails(
            @Param("memberId") Integer memberId,
            @Param("status") BookingStatus status);

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.member
            JOIN FETCH b.session s
            JOIN FETCH s.serviceType
            JOIN FETCH s.trainer
            WHERE b.bookingId = :bookingId
              AND b.member.memberId = :memberId
            """)
    Optional<Booking> findByBookingIdAndMemberIdWithDetails(
            @Param("bookingId") Integer bookingId,
            @Param("memberId") Integer memberId);

    boolean existsByMember_MemberIdAndSession_SessionIdAndBookingStatus(
            Integer memberId, Integer sessionId, BookingStatus bookingStatus);

    List<Booking> findBySession_SessionId(Integer sessionId);

    void deleteByMember_MemberId(Integer memberId);

    void deleteBySession_SessionId(Integer sessionId);
}
