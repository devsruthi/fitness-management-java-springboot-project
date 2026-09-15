package com.fitness.management.service.impl;

import com.fitness.management.dto.request.BookingRequest;
import com.fitness.management.dto.response.BookingResponse;
import com.fitness.management.entity.Booking;
import com.fitness.management.entity.Member;
import com.fitness.management.entity.Session;
import com.fitness.management.entity.enums.BookingStatus;
import com.fitness.management.entity.enums.SessionStatus;
import com.fitness.management.entity.enums.SubscriptionStatus;
import com.fitness.management.exception.BusinessRuleException;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.BookingRepository;
import com.fitness.management.repository.MemberRepository;
import com.fitness.management.repository.MemberSubscriptionRepository;
import com.fitness.management.repository.SessionRepository;
import com.fitness.management.service.BookingService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;
    private final MemberSubscriptionRepository memberSubscriptionRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            MemberRepository memberRepository,
            SessionRepository sessionRepository,
            MemberSubscriptionRepository memberSubscriptionRepository) {
        this.bookingRepository = bookingRepository;
        this.memberRepository = memberRepository;
        this.sessionRepository = sessionRepository;
        this.memberSubscriptionRepository = memberSubscriptionRepository;
    }

    @Override
    @Transactional
    public BookingResponse bookSession(Integer memberId, BookingRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        boolean hasActiveSubscription = memberSubscriptionRepository
                .existsByMember_MemberIdAndSubscriptionStatus(memberId, SubscriptionStatus.ACTIVE);
        if (!hasActiveSubscription) {
            throw new BusinessRuleException("You do not have an active subscription!");
        }

        Session session = sessionRepository.findById(request.sessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        if (session.getSessionStatus() == SessionStatus.COMPLETED
                || session.getSessionStatus() == SessionStatus.CANCELLED) {
            throw new BusinessRuleException("Session is already completed or cancelled!");
        }

        boolean alreadyBooked = bookingRepository.existsByMember_MemberIdAndSession_SessionIdAndBookingStatus(
                memberId, request.sessionId(), BookingStatus.BOOKED);
        if (alreadyBooked) {
            throw new BusinessRuleException("You have already booked this session!");
        }

        Booking booking = new Booking();
        booking.setMember(member);
        booking.setSession(session);
        booking.setBookingCreatedTime(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.BOOKED);

        Booking saved = bookingRepository.save(booking);
        return BookingResponse.from(saved, "Congrats, you have successfully booked the session");
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
