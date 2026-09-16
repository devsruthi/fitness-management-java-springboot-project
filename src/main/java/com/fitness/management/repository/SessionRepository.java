package com.fitness.management.repository;

import com.fitness.management.entity.Session;
import com.fitness.management.entity.enums.SessionStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    @EntityGraph(attributePaths = {"serviceType", "trainer"})
    List<Session> findBySessionStatusOrderBySessionDateAscStartTimeAsc(SessionStatus sessionStatus);

    @Query("""
            SELECT DISTINCT s FROM Session s
            JOIN FETCH s.serviceType
            JOIN FETCH s.trainer
            WHERE s.sessionStatus = :status
              AND (s.sessionDate > :today
                   OR (s.sessionDate = :today AND s.startTime >= :now))
            ORDER BY s.sessionDate ASC, s.startTime ASC
            """)
    List<Session> findUpcomingScheduledSessions(
            @Param("status") SessionStatus status,
            @Param("today") LocalDate today,
            @Param("now") LocalTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM Session_updations WHERE session_id = :sessionId", nativeQuery = true)
    void deleteSessionUpdationsBySessionId(@Param("sessionId") Integer sessionId);
}
