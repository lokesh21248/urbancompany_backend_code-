package com.urbanservices.backend.controller;

import com.urbanservices.backend.dto.*;
import com.urbanservices.backend.service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/providers")
@CrossOrigin(origins = "*")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping
    public ResponseEntity<List<ProviderProfileDTO>> getAllProviders(
            @RequestParam(name = "verificationStatus", required = false) String verificationStatus,
            @RequestParam(name = "status", required = false) String status) {
        // support both param names
        String filter = verificationStatus != null ? verificationStatus : status;
        return ResponseEntity.ok(providerService.getAllProviders(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderProfileDTO> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ProviderProfileDTO> getProviderByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(providerService.getProviderByUserId(userId));
    }

    @PutMapping("/{id}/services")
    public ResponseEntity<ProviderProfileDTO> updateProviderServices(
            @PathVariable Long id,
            @RequestBody ProviderServiceUpdateRequest request) {
        return ResponseEntity.ok(providerService.updateProviderServices(id, request.getServiceIds()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProviderProfileDTO> updateProviderStatus(
            @PathVariable Long id,
            @RequestBody ProviderStatusUpdateRequest request) {
        return ResponseEntity.ok(providerService.updateProviderStatus(id, request));
    }

    @GetMapping("/{id}/matching-orders")
    public ResponseEntity<List<BookingDTO>> getMatchingOrdersForProvider(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.getMatchingOrdersForProvider(id));
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingDTO>> getProviderBookings(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.getProviderBookings(id));
    }
}
