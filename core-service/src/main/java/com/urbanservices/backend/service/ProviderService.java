package com.urbanservices.backend.service;

import com.urbanservices.backend.dto.BookingDTO;
import com.urbanservices.backend.dto.ProviderProfileDTO;
import com.urbanservices.backend.dto.ProviderStatusUpdateRequest;

import java.util.List;

public interface ProviderService {
    List<ProviderProfileDTO> getAllProviders(String verificationStatus);
    ProviderProfileDTO getProviderById(Long id);
    ProviderProfileDTO getProviderByUserId(Long userId);
    ProviderProfileDTO updateProviderServices(Long providerId, List<Long> serviceIds);
    ProviderProfileDTO updateProviderStatus(Long providerId, ProviderStatusUpdateRequest request);
    List<BookingDTO> getMatchingOrdersForProvider(Long providerId);
    List<BookingDTO> getProviderBookings(Long providerId);
}
