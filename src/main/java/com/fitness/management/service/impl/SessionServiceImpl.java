package com.fitness.management.service.impl;

import com.fitness.management.dto.response.MessageResponse;
import com.fitness.management.dto.response.SessionResponse;
import com.fitness.management.entity.Booking;
import com.fitness.management.entity.Session;
import com.fitness.management.entity.enums.BookingStatus;
import com.fitness.management.entity.enums.SessionStatus;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.BookingRepository;
import com.fitness.management.repository.SessionRepository;
import com.fitness.management.service.SessionService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final BookingRepository bookingRepository;

    public SessionServiceImpl(SessionRepository sessionRepository, BookingRepository bookingRepository) {
        this.sessionRepository = sessionRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<SessionResponse> listScheduledSessions() {
        return sessionRepository
                .findBySessionStatusOrderBySessionDateAscStartTimeAsc(SessionStatus.SCHEDULED)
                .stream()
                .map(SessionResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public MessageResponse cancelSession(Integer sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        session.setSessionStatus(SessionStatus.CANCELLED);
        session.setSessionCancelledTime(LocalDateTime.now());
        sessionRepository.save(session);

        List<Booking> bookings = bookingRepository.findBySession_SessionId(sessionId);
        for (Booking booking : bookings) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.setBookingCancelledTime(LocalDateTime.now());
        }
        bookingRepository.saveAll(bookings);

        return new MessageResponse("Session & Bookings cancelled successfully");
    }
}
