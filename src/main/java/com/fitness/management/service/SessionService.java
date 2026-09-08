package com.fitness.management.service;

import com.fitness.management.dto.response.MessageResponse;
import com.fitness.management.dto.response.SessionResponse;
import java.util.List;

public interface SessionService {

    List<SessionResponse> listScheduledSessions();

    MessageResponse cancelSession(Integer sessionId);
}
