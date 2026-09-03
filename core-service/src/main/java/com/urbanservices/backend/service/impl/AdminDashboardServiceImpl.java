package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.domain.entity.AppConfig;
import com.urbanservices.backend.domain.entity.Booking;
import com.urbanservices.backend.domain.enums.BookingStatus;
import com.urbanservices.backend.domain.enums.Role;
import com.urbanservices.backend.domain.repository.AppConfigRepository;
import com.urbanservices.backend.domain.repository.BookingRepository;
import com.urbanservices.backend.domain.repository.ProviderProfileRepository;
import com.urbanservices.backend.domain.repository.UserRepository;
import com.urbanservices.backend.dto.AppConfigDTO;
import com.urbanservices.backend.dto.BookingDTO;
import com.urbanservices.backend.dto.BookingItemDTO;
import com.urbanservices.backend.dto.DashboardStatsDTO;
import com.urbanservices.backend.dto.ServiceItemDTO;
import com.urbanservices.backend.client.CatalogServiceClient;
import com.urbanservices.backend.service.AdminDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final BookingRepository bookingRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final UserRepository userRepository;
    private final AppConfigRepository appConfigRepository;
    private final CatalogServiceClient catalogServiceClient;

    public AdminDashboardServiceImpl(BookingRepository bookingRepository,
                                    ProviderProfileRepository providerProfileRepository,
                                    UserRepository userRepository,
                                    AppConfigRepository appConfigRepository,
                                    CatalogServiceClient catalogServiceClient) {
        this.bookingRepository = bookingRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.userRepository = userRepository;
        this.appConfigRepository = appConfigRepository;
        this.catalogServiceClient = catalogServiceClient;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO dto = new DashboardStatsDTO();

        List<Booking> allBookings = bookingRepository.findAll();
        dto.setTotalBookings((long) allBookings.size());

        BigDecimal totalRevenue = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                .map(Booking::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalRevenue(totalRevenue);

        long active = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.PENDING || b.getStatus() == BookingStatus.ACCEPTED || b.getStatus() == BookingStatus.IN_PROGRESS)
                .count();
        dto.setActiveBookings(active);

        long completed = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                .count();
        dto.setCompletedBookings(completed);

        dto.setTotalProviders((long) providerProfileRepository.findAll().size());
        dto.setOnlineProviders(providerProfileRepository.countByIsOnlineTrue());
        dto.setTotalCustomers(userRepository.countByRole(Role.CUSTOMER));

        List<BookingDTO> recent = allBookings.stream()
                .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                .limit(10)
                .map(this::mapBookingToDTO)
                .collect(Collectors.toList());
        dto.setRecentBookings(recent);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppConfigDTO> getAllConfigs() {
        return appConfigRepository.findAll().stream().map(c -> {
            AppConfigDTO dto = new AppConfigDTO();
            dto.setId(c.getId());
            dto.setConfigKey(c.getConfigKey());
            dto.setConfigValue(c.getConfigValue());
            dto.setValueType(c.getValueType());
            dto.setDescription(c.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public AppConfigDTO updateConfig(String key, String value) {
        AppConfig config = appConfigRepository.findByConfigKey(key)
                .orElseThrow(() -> new RuntimeException("Config not found for key: " + key));
        config.setConfigValue(value);
        AppConfig saved = appConfigRepository.save(config);

        AppConfigDTO dto = new AppConfigDTO();
        dto.setId(saved.getId());
        dto.setConfigKey(saved.getConfigKey());
        dto.setConfigValue(saved.getConfigValue());
        dto.setValueType(saved.getValueType());
        dto.setDescription(saved.getDescription());
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
                itemDto.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                return itemDto;
            }).collect(Collectors.toList()));
        } else {
            dto.setItems(new ArrayList<>());
        }
        return dto;
    }
}
