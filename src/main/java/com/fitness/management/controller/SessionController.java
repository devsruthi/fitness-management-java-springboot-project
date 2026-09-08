package com.fitness.management.controller;

import com.fitness.management.dto.response.MessageResponse;
import com.fitness.management.dto.response.SessionResponse;
import com.fitness.management.service.SessionService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> listScheduledSessions() {
        return ResponseEntity.ok(sessionService.listScheduledSessions());
    }

    @PutMapping("/{sessionId}/cancel")
    public ResponseEntity<MessageResponse> cancelSession(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(sessionService.cancelSession(sessionId));
    }
}
