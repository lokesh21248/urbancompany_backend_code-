package com.urbanservices.backend.controller;

import com.urbanservices.backend.dto.BookingCreateRequest;
import com.urbanservices.backend.dto.BookingDTO;
import com.urbanservices.backend.dto.BookingStatusUpdateRequest;
import com.urbanservices.backend.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "customerId", required = false) Long customerId,
            @RequestParam(name = "providerId", required = false) Long providerId) {
        return ResponseEntity.ok(bookingService.getAllBookings(status, customerId, providerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody BookingStatusUpdateRequest request) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, request));
    }

    @PutMapping("/{id}/assign/{providerId}")
    public ResponseEntity<BookingDTO> assignProvider(
            @PathVariable Long id,
            @PathVariable Long providerId) {
        return ResponseEntity.ok(bookingService.assignProvider(id, providerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long id,
            @RequestParam(name = "reason", defaultValue = "Cancelled by user") String reason) {
        bookingService.cancelBooking(id, reason);
        return ResponseEntity.noContent().build();
    }
}
