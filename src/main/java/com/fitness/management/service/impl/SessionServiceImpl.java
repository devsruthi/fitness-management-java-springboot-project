package com.fitness.management.service.impl;

import com.fitness.management.dto.response.MessageResponse;
import com.fitness.management.dto.response.SessionResponse;
import com.fitness.management.entity.enums.SessionStatus;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.SessionRepository;
import com.fitness.management.repository.StoredProcedureRepository;
import com.fitness.management.service.SessionService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final StoredProcedureRepository storedProcedureRepository;

    public SessionServiceImpl(
            SessionRepository sessionRepository,
            StoredProcedureRepository storedProcedureRepository) {
        this.sessionRepository = sessionRepository;
        this.storedProcedureRepository = storedProcedureRepository;
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
    public MessageResponse cancelSession(Integer sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new ResourceNotFoundException("Session not found");
        }
        storedProcedureRepository.cancelSession(sessionId);
        return new MessageResponse("Session & Bookings cancelled successfully");
    }
}
