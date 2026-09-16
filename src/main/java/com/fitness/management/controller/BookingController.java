package com.fitness.management.controller;

import com.fitness.management.dto.request.BookingRequest;
import com.fitness.management.dto.response.BookingResponse;
import com.fitness.management.service.BookingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/members/bookings")
    public ResponseEntity<BookingResponse> bookSession(
            @RequestParam Integer memberId,
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookSession(memberId, request));
    }

    @PutMapping("/members/bookings/cancel")
    public ResponseEntity<BookingResponse> cancelBookingByMember(
            @RequestParam Integer memberId,
            @RequestParam Integer bookingId) {
        return ResponseEntity.ok(bookingService.cancelBookingByMember(memberId, bookingId));
    }
}
