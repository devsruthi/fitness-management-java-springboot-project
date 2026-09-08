package com.fitness.management.service;

import com.fitness.management.dto.request.BookingRequest;
import com.fitness.management.dto.response.BookingResponse;
import java.util.List;

public interface BookingService {

    BookingResponse bookSession(Integer memberId, BookingRequest request);

    List<BookingResponse> listMemberBookings(Integer memberId);
}
