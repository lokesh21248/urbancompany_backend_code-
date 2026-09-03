package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.domain.entity.Booking;
import com.urbanservices.backend.domain.entity.ProviderProfile;
import com.urbanservices.backend.domain.enums.VerificationStatus;
import com.urbanservices.backend.domain.repository.BookingRepository;
import com.urbanservices.backend.domain.repository.ProviderProfileRepository;
import com.urbanservices.backend.client.CatalogServiceClient;
import com.urbanservices.backend.dto.*;
import com.urbanservices.backend.service.ProviderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProviderServiceImpl implements ProviderService {

    private final ProviderProfileRepository providerProfileRepository;
    private final CatalogServiceClient catalogServiceClient;
    private final BookingRepository bookingRepository;

    public ProviderServiceImpl(ProviderProfileRepository providerProfileRepository,
                               CatalogServiceClient catalogServiceClient,
                               BookingRepository bookingRepository) {
        this.providerProfileRepository = providerProfileRepository;
        this.catalogServiceClient = catalogServiceClient;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProviderProfileDTO> getAllProviders(String verificationStatus) {
        List<ProviderProfile> profiles;
        if (verificationStatus != null && !verificationStatus.isBlank()) {
            profiles = providerProfileRepository.findByVerificationStatus(VerificationStatus.valueOf(verificationStatus.toUpperCase()));
        } else {
            profiles = providerProfileRepository.findAll();
        }
        return profiles.stream().map(this::mapProfileToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderProfileDTO getProviderById(Long id) {
        ProviderProfile profile = providerProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + id));
        return mapProfileToDTO(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public ProviderProfileDTO getProviderByUserId(Long userId) {
        ProviderProfile profile = providerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found for user ID: " + userId));
        return mapProfileToDTO(profile);
    }

    @Override
    public ProviderProfileDTO updateProviderServices(Long providerId, List<Long> serviceIds) {
        ProviderProfile profile = providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + providerId));

        if (serviceIds != null && !serviceIds.isEmpty()) {
            // Call catalog service to validate if needed, for now just set IDs
            profile.setServiceItemIds(new HashSet<>(serviceIds));
        } else {
            profile.setServiceItemIds(new HashSet<>());
        }

        ProviderProfile saved = providerProfileRepository.save(profile);
        return mapProfileToDTO(saved);
    }

    @Override
    public ProviderProfileDTO updateProviderStatus(Long providerId, ProviderStatusUpdateRequest request) {
        ProviderProfile profile = providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + providerId));

        if (request.getIsOnline() != null) {
            profile.setOnline(request.getIsOnline());
        }
        if (request.getVerificationStatus() != null) {
            profile.setVerificationStatus(VerificationStatus.valueOf(request.getVerificationStatus().toUpperCase()));
        }

        ProviderProfile saved = providerProfileRepository.save(profile);
        return mapProfileToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getMatchingOrdersForProvider(Long providerId) {
        ProviderProfile profile = providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + providerId));

        Set<Long> services = profile.getServiceItemIds();
        if (services.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> serviceIds = new ArrayList<>(services);
        List<Booking> matchingBookings = bookingRepository.findPendingBookingsByServiceIds(serviceIds);

        return matchingBookings.stream().map(this::mapBookingToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getProviderBookings(Long providerId) {
        ProviderProfile profile = providerProfileRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + providerId));

        Long userId = profile.getUser() != null ? profile.getUser().getId() : providerId;
        List<Booking> bookings = bookingRepository.findByProviderIdOrderByScheduledTimeDesc(userId);
        return bookings.stream().map(this::mapBookingToDTO).collect(Collectors.toList());
    }

    private ProviderProfileDTO mapProfileToDTO(ProviderProfile p) {
        ProviderProfileDTO dto = new ProviderProfileDTO();
        dto.setId(p.getId());
        if (p.getUser() != null) {
            dto.setUserId(p.getUser().getId());
            dto.setFullName(p.getUser().getFullName());
            dto.setEmail(p.getUser().getEmail());
            dto.setPhoneNumber(p.getUser().getPhoneNumber());
        }
        dto.setBusinessName(p.getBusinessName());
        dto.setBio(p.getBio());
        dto.setRating(p.getRating());
        dto.setTotalReviews(p.getTotalReviews());
        dto.setVerificationStatus(p.getVerificationStatus() != null ? p.getVerificationStatus().name() : "PENDING");
        dto.setIsOnline(p.getOnline());
        dto.setLatitude(p.getLatitude());
        dto.setLongitude(p.getLongitude());

        if (p.getServiceItemIds() != null && !p.getServiceItemIds().isEmpty()) {
            try {
                List<ServiceItemDTO> serviceItems = catalogServiceClient.getServicesByIds(new ArrayList<>(p.getServiceItemIds()));
                dto.setServices(serviceItems);
            } catch (Exception e) {
                // If catalog service is down or fails, we just return empty services for now
                dto.setServices(new ArrayList<>());
            }
        } else {
            dto.setServices(new ArrayList<>());
        }
        return dto;
    }

    private BookingDTO mapBookingToDTO(Booking b) {
        BookingDTO dto = new BookingDTO();
        dto.setId(b.getId());
        if (b.getCustomer() != null) {
            dto.setCustomerId(b.getCustomer().getId());
            dto.setCustomerName(b.getCustomer().getFullName());
            dto.setCustomerPhone(b.getCustomer().getPhoneNumber());
        }
        if (b.getProvider() != null) {
            dto.setProviderId(b.getProvider().getId());
            dto.setProviderName(b.getProvider().getFullName());
            dto.setProviderPhone(b.getProvider().getPhoneNumber());
        }
        if (b.getAddress() != null) {
            dto.setAddressId(b.getAddress().getId());
            dto.setAddressText(b.getAddress().getAddressLine1() + ", " + b.getAddress().getCity());
            dto.setCity(b.getAddress().getCity());
        }
        dto.setStatus(b.getStatus() != null ? b.getStatus().name() : "PENDING");
        dto.setScheduledTime(b.getScheduledTime());
        dto.setTotalAmount(b.getTotalAmount());
        dto.setDiscountAmount(b.getDiscountAmount());
        dto.setFinalAmount(b.getFinalAmount());
        dto.setCancellationReason(b.getCancellationReason());
        dto.setCreatedAt(b.getCreatedAt());

        if (b.getItems() != null) {
            dto.setItems(b.getItems().stream().map(item -> {
                BookingItemDTO itemDto = new BookingItemDTO();
                itemDto.setId(item.getId());
                if (item.getServiceItemId() != null) {
                    itemDto.setServiceId(item.getServiceItemId());
                    try {
                        ServiceItemDTO serviceItem = catalogServiceClient.getServiceById(item.getServiceItemId());
                        if (serviceItem != null) {
                            itemDto.setServiceName(serviceItem.getName());
                        }
                    } catch (Exception e) {
                        itemDto.setServiceName("Unknown Service");
                    }
                }
                itemDto.setPrice(item.getPrice());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setTotalPrice(item.getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
                return itemDto;
            }).collect(Collectors.toList()));
        } else {
            dto.setItems(new ArrayList<>());
        }
        return dto;
    }
}
