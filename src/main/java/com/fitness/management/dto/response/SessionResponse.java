package com.fitness.management.dto.response;

import com.fitness.management.entity.Session;
import com.fitness.management.entity.enums.ServiceMode;
import com.fitness.management.entity.enums.SessionMode;
import com.fitness.management.entity.enums.SessionStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record SessionResponse(
        Integer sessionId,
        LocalDate sessionDate,
        LocalTime startTime,
        Integer durationInMinutes,
        SessionMode sessionMode,
        String sessionRoom,
        SessionStatus sessionStatus,
        String serviceTypeName,
        ServiceMode serviceMode,
        Integer maxParticipants,
        String trainerName
) {
    public static SessionResponse from(Session session) {
        String trainerName = session.getTrainer().getFirstName() + " " + session.getTrainer().getLastName();
        return new SessionResponse(
                session.getSessionId(),
                session.getSessionDate(),
                session.getStartTime(),
                session.getDurationInMinutes(),
                session.getSessionMode(),
                session.getSessionRoom(),
                session.getSessionStatus(),
                session.getServiceType().getServiceTypeName(),
                session.getServiceType().getServiceMode(),
                session.getServiceType().getMaxParticipants(),
                trainerName);
    }
}
