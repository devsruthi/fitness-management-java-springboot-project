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

    @Query("""
            SELECT st.serviceTypeId,
                   st.serviceTypeName,
                   st.serviceTypeDescription,
                   st.serviceMode,
                   st.maxParticipants,
                   st.serviceTypeStatus,
                   COUNT(b.bookingId)
            FROM Booking b
            JOIN b.session s
            JOIN s.serviceType st
            GROUP BY st.serviceTypeId,
                     st.serviceTypeName,
                     st.serviceTypeDescription,
                     st.serviceMode,
                     st.maxParticipants,
                     st.serviceTypeStatus
            ORDER BY COUNT(b.bookingId) DESC, st.serviceTypeName ASC
            """)
    List<Object[]> countBookingsByServiceType();

    @Query("""
            SELECT s.sessionId, COUNT(b.bookingId)
            FROM Booking b
            JOIN b.session s
            WHERE b.bookingStatus = :status
            GROUP BY s.sessionId
            ORDER BY COUNT(b.bookingId) DESC, s.sessionId ASC
            """)
    List<Object[]> countBookingsBySessionAndStatus(@Param("status") BookingStatus status);

    void deleteByMember_MemberId(Integer memberId);

    void deleteBySession_SessionId(Integer sessionId);
}
