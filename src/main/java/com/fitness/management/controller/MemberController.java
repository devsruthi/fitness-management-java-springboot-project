package com.fitness.management.controller;

import com.fitness.management.dto.request.MemberLoginRequest;
import com.fitness.management.dto.request.MemberRegistrationRequest;
import com.fitness.management.dto.response.BookingResponse;
import com.fitness.management.dto.response.MemberAuthResponse;
import com.fitness.management.dto.response.MemberResponse;
import com.fitness.management.dto.response.MemberSubscriptionResponse;
import com.fitness.management.dto.response.MemberWithSubscriptionResponse;
import com.fitness.management.dto.response.MessageResponse;
import com.fitness.management.dto.response.PaymentResponse;
import com.fitness.management.service.BookingService;
import com.fitness.management.service.MemberService;
import com.fitness.management.service.SubscriptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final BookingService bookingService;
    private final SubscriptionService subscriptionService;

    public MemberController(
            MemberService memberService,
            BookingService bookingService,
            SubscriptionService subscriptionService) {
        this.memberService = memberService;
        this.bookingService = bookingService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(params = "!memberId")
    public ResponseEntity<List<MemberResponse>> listMembers() {
        return ResponseEntity.ok(memberService.listMembers());
    }

    @GetMapping("/active-subscriptions")
    public ResponseEntity<List<MemberWithSubscriptionResponse>> listMembersWithActiveSubscription() {
        return ResponseEntity.ok(subscriptionService.listMembersWithActiveSubscription());
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<MemberWithSubscriptionResponse>> listMembersExpiringSoon(
            @RequestParam Integer days) {
        return ResponseEntity.ok(subscriptionService.listMembersExpiringSoon(days));
    }

    @PostMapping("/register")
    public ResponseEntity<MemberAuthResponse> register(@Valid @RequestBody MemberRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberAuthResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @GetMapping(params = "memberId")
    public ResponseEntity<MemberResponse> getMember(@RequestParam Integer memberId) {
        return ResponseEntity.ok(memberService.getMember(memberId));
    }

    @GetMapping("/subscriptions/current")
    public ResponseEntity<MemberSubscriptionResponse> getCurrentSubscription(@RequestParam Integer memberId) {
        return ResponseEntity.ok(subscriptionService.getCurrentOrLatestSubscription(memberId));
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> listPayments(@RequestParam Integer memberId) {
        return ResponseEntity.ok(subscriptionService.listMemberPayments(memberId));
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> listBookings(@RequestParam Integer memberId) {
        return ResponseEntity.ok(bookingService.listMemberBookings(memberId));
    }

    @GetMapping("/bookings/upcoming")
    public ResponseEntity<List<BookingResponse>> listUpcomingBookings(@RequestParam Integer memberId) {
        return ResponseEntity.ok(bookingService.listUpcomingMemberBookings(memberId));
    }

    @GetMapping("/bookings/cancelled")
    public ResponseEntity<List<BookingResponse>> listCancelledBookings(@RequestParam Integer memberId) {
        return ResponseEntity.ok(bookingService.listCancelledMemberBookings(memberId));
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteMember(@RequestParam Integer memberId) {
        return ResponseEntity.ok(memberService.deleteMember(memberId));
    }
}
