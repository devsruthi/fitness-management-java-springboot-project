package com.fitness.management.repository;

import com.fitness.management.entity.Session;
import com.fitness.management.entity.enums.SessionStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    @Query("""
            SELECT s FROM Session s
            JOIN FETCH s.serviceType
            JOIN FETCH s.trainer
            WHERE s.sessionStatus = :status
            ORDER BY s.sessionDate ASC, s.startTime ASC
            """)
    List<Session> findByStatusWithDetails(@Param("status") SessionStatus status);

    @Query("""
            SELECT s FROM Session s
            JOIN FETCH s.serviceType
            JOIN FETCH s.trainer
            WHERE s.sessionId = :sessionId
            """)
    Optional<Session> findWithDetailsBySessionId(@Param("sessionId") Integer sessionId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM Session_updations WHERE session_id = :sessionId", nativeQuery = true)
    void deleteSessionUpdationsBySessionId(@Param("sessionId") Integer sessionId);
}
