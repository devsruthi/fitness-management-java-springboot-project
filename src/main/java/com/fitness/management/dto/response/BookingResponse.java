package com.fitness.management.dto.response;

import com.fitness.management.entity.Booking;
import com.fitness.management.entity.enums.BookingStatus;
import java.time.LocalDateTime;

public record BookingResponse(
        Integer bookingId,
        Integer memberId,
        Integer sessionId,
        BookingStatus bookingStatus,
        LocalDateTime bookingCreatedTime,
        LocalDateTime bookingCancelledTime,
        String message,
        SessionResponse session
) {
    public static BookingResponse from(Booking booking, String message) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getMember().getMemberId(),
                booking.getSession().getSessionId(),
                booking.getBookingStatus(),
                booking.getBookingCreatedTime(),
                booking.getBookingCancelledTime(),
                message,
                SessionResponse.from(booking.getSession()));
    }
}
