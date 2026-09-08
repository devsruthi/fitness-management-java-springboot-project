package com.fitness.management.service.impl;

import com.fitness.management.dto.request.BookingRequest;
import com.fitness.management.dto.response.BookingResponse;
import com.fitness.management.entity.Booking;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.BookingRepository;
import com.fitness.management.repository.MemberRepository;
import com.fitness.management.repository.StoredProcedureRepository;
import com.fitness.management.service.BookingService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final StoredProcedureRepository storedProcedureRepository;
    private final BookingRepository bookingRepository;
    private final MemberRepository memberRepository;

    public BookingServiceImpl(
            StoredProcedureRepository storedProcedureRepository,
            BookingRepository bookingRepository,
            MemberRepository memberRepository) {
        this.storedProcedureRepository = storedProcedureRepository;
        this.bookingRepository = bookingRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public BookingResponse bookSession(Integer memberId, BookingRequest request) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found");
        }
        storedProcedureRepository.bookSession(memberId, request.sessionId());
        Booking booking = bookingRepository
                .findTopByMember_MemberIdAndSession_SessionIdOrderByBookingIdDesc(memberId, request.sessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking was created but could not be loaded"));
        return BookingResponse.from(booking, "Congrats, you have successfully booked the session");
    }

    @Override
    public List<BookingResponse> listMemberBookings(Integer memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found");
        }
        return bookingRepository.findByMember_MemberIdOrderByBookingCreatedTimeDesc(memberId).stream()
                .map(booking -> BookingResponse.from(booking, null))
                .toList();
    }
}
