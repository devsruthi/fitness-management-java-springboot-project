package com.fitness.management.controller;

import com.fitness.management.dto.request.BookingRequest;
import com.fitness.management.dto.response.BookingResponse;
import com.fitness.management.service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> listAllBookings() {
        return ResponseEntity.ok(bookingService.listAllBookings());
    }

    @GetMapping("/bookings/cancelled")
    public ResponseEntity<List<BookingResponse>> listCancelledBookings() {
        return ResponseEntity.ok(bookingService.listCancelledBookings());
    }

    @PostMapping("/members/{memberId}/bookings")
    public ResponseEntity<BookingResponse> bookSession(
            @PathVariable Integer memberId,
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookSession(memberId, request));
    }

    @GetMapping("/members/{memberId}/bookings")
    public ResponseEntity<List<BookingResponse>> listMemberBookings(@PathVariable Integer memberId) {
        return ResponseEntity.ok(bookingService.listMemberBookings(memberId));
    }

    @GetMapping("/members/{memberId}/bookings/upcoming")
    public ResponseEntity<List<BookingResponse>> listUpcomingMemberBookings(@PathVariable Integer memberId) {
        return ResponseEntity.ok(bookingService.listUpcomingMemberBookings(memberId));
    }

    @GetMapping("/members/{memberId}/bookings/cancelled")
    public ResponseEntity<List<BookingResponse>> listCancelledMemberBookings(@PathVariable Integer memberId) {
        return ResponseEntity.ok(bookingService.listCancelledMemberBookings(memberId));
    }

    @PutMapping("/members/{memberId}/bookings/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBookingByMember(
            @PathVariable Integer memberId,
            @PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingService.cancelBookingByMember(memberId, bookingId));
    }
}
