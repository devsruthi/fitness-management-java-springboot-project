package com.fitness.management.repository;

import com.fitness.management.entity.Session;
import com.fitness.management.entity.enums.SessionStatus;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    @EntityGraph(attributePaths = {"serviceType", "trainer"})
    List<Session> findBySessionStatusOrderBySessionDateAscStartTimeAsc(SessionStatus sessionStatus);
}
