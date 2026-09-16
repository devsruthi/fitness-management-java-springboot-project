package com.fitness.management.service;

import com.fitness.management.dto.request.BookingRequest;
import com.fitness.management.dto.response.BookingResponse;
import java.util.List;

public interface BookingService {

    BookingResponse bookSession(Integer memberId, BookingRequest request);

    List<BookingResponse> listAllBookings();

    List<BookingResponse> listCancelledBookings();

    List<BookingResponse> listMemberBookings(Integer memberId);

    List<BookingResponse> listUpcomingMemberBookings(Integer memberId);

    List<BookingResponse> listCancelledMemberBookings(Integer memberId);

    BookingResponse cancelBookingByMember(Integer memberId, Integer bookingId);
}
