package com.fitness.management.entity;

import com.fitness.management.entity.enums.BookingCancelledBy;
import com.fitness.management.entity.enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bookings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "session_id"})
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(name = "booking_created_time", nullable = false)
    private LocalDateTime bookingCreatedTime;

    @Column(name = "booking_cancelled_time")
    private LocalDateTime bookingCancelledTime;

    @Column(name = "booking_cancelled_reason", length = 250)
    private String bookingCancelledReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_cancelled_by")
    private BookingCancelledBy bookingCancelledBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public LocalDateTime getBookingCreatedTime() {
        return bookingCreatedTime;
    }

    public void setBookingCreatedTime(LocalDateTime bookingCreatedTime) {
        this.bookingCreatedTime = bookingCreatedTime;
    }

    public LocalDateTime getBookingCancelledTime() {
        return bookingCancelledTime;
    }

    public void setBookingCancelledTime(LocalDateTime bookingCancelledTime) {
        this.bookingCancelledTime = bookingCancelledTime;
    }

    public String getBookingCancelledReason() {
        return bookingCancelledReason;
    }

    public void setBookingCancelledReason(String bookingCancelledReason) {
        this.bookingCancelledReason = bookingCancelledReason;
    }

    public BookingCancelledBy getBookingCancelledBy() {
        return bookingCancelledBy;
    }

    public void setBookingCancelledBy(BookingCancelledBy bookingCancelledBy) {
        this.bookingCancelledBy = bookingCancelledBy;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
