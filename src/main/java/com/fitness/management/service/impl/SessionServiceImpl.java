package com.fitness.management.service.impl;

import com.fitness.management.dto.response.MessageResponse;
import com.fitness.management.dto.response.SessionResponse;
import com.fitness.management.entity.Booking;
import com.fitness.management.entity.Session;
import com.fitness.management.entity.enums.BookingCancelledBy;
import com.fitness.management.entity.enums.BookingStatus;
import com.fitness.management.entity.enums.SessionStatus;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.BookingRepository;
import com.fitness.management.repository.SessionRepository;
import com.fitness.management.service.SessionService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Transactional(readOnly = true)
    public List<SessionResponse> listScheduledSessions() {
        return mapSessions(sessionRepository.findByStatusWithDetails(SessionStatus.SCHEDULED));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> listUpcomingScheduledSessions() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        return sessionRepository.findByStatusWithDetails(SessionStatus.SCHEDULED).stream()
                .filter(session -> {
                    LocalDate sessionDate = session.getSessionDate();
                    LocalTime startTime = session.getStartTime();
                    return sessionDate.isAfter(today)
                            || (sessionDate.isEqual(today) && !startTime.isBefore(now));
                })
                .map(SessionResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> listCompletedSessions() {
        return mapSessions(sessionRepository.findByStatusWithDetails(SessionStatus.COMPLETED));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> listCancelledSessions() {
        return mapSessions(sessionRepository.findByStatusWithDetails(SessionStatus.CANCELLED));
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
        LocalDateTime cancelledAt = LocalDateTime.now();
        for (Booking booking : bookings) {
            if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
                continue;
            }
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.setBookingCancelledTime(cancelledAt);
            booking.setBookingCancelledBy(BookingCancelledBy.SYSTEM);
            booking.setBookingCancelledReason("Session cancelled");
        }
        bookingRepository.saveAll(bookings);

        return new MessageResponse("Session and related bookings cancelled successfully");
    }

    @Override
    @Transactional
    public MessageResponse deleteSession(Integer sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new ResourceNotFoundException("Session not found");
        }

        sessionRepository.deleteSessionUpdationsBySessionId(sessionId);
        bookingRepository.deleteBySession_SessionId(sessionId);
        sessionRepository.deleteById(sessionId);

        return new MessageResponse("Session and related bookings and session updates deleted");
    }

    private List<SessionResponse> mapSessions(List<Session> sessions) {
        return sessions.stream().map(SessionResponse::from).toList();
    }
}
