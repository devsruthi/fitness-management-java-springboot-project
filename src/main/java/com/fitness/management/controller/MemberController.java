package com.fitness.management.controller;

import com.fitness.management.dto.request.MemberLoginRequest;
import com.fitness.management.dto.request.MemberRegistrationRequest;
import com.fitness.management.dto.response.MemberAuthResponse;
import com.fitness.management.dto.response.MemberResponse;
import com.fitness.management.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberAuthResponse> register(@Valid @RequestBody MemberRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberAuthResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Integer memberId) {
        return ResponseEntity.ok(memberService.getMember(memberId));
    }
}
