package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.domain.entity.*;
import com.urbanservices.backend.domain.enums.BookingStatus;
import com.urbanservices.backend.domain.repository.*;
import com.urbanservices.backend.client.CatalogServiceClient;
import com.urbanservices.backend.dto.*;
import com.urbanservices.backend.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CatalogServiceClient catalogServiceClient;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              AddressRepository addressRepository,
                              CatalogServiceClient catalogServiceClient) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.catalogServiceClient = catalogServiceClient;
    }

    @Override
    public BookingDTO createBooking(BookingCreateRequest request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + request.getCustomerId()));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + request.getAddressId()));

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setAddress(address);
        booking.setStatus(BookingStatus.PENDING);
        booking.setScheduledTime(request.getScheduledTime() != null ? request.getScheduledTime() : LocalDateTime.now().plusHours(2));

        List<BookingItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        if (request.getItems() != null) {
            for (BookingCreateRequest.ItemRequest itemReq : request.getItems()) {
                ServiceItemDTO service = catalogServiceClient.getServiceById(itemReq.getServiceId());
                if (service == null) {
                    throw new RuntimeException("Service not found with ID: " + itemReq.getServiceId());
                }

                int qty = itemReq.getQuantity() != null && itemReq.getQuantity() > 0 ? itemReq.getQuantity() : 1;
                BigDecimal itemCost = service.getBasePrice().multiply(BigDecimal.valueOf(qty));
                total = total.add(itemCost);

                BookingItem item = new BookingItem();
                item.setBooking(booking);
                item.setServiceItemId(itemReq.getServiceId());
                item.setPrice(service.getBasePrice());
                item.setQuantity(qty);
                items.add(item);
            }
        }

        booking.setItems(items);
        booking.setTotalAmount(total);
        booking.setDiscountAmount(BigDecimal.ZERO);
        booking.setFinalAmount(total);

        Booking saved = bookingRepository.save(booking);
        return mapBookingToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getAllBookings(String status, Long customerId, Long providerId) {
        List<Booking> bookings;
        if (customerId != null) {
            bookings = bookingRepository.findByCustomerIdOrderByScheduledTimeDesc(customerId);
        } else if (providerId != null) {
            bookings = bookingRepository.findByProviderIdOrderByScheduledTimeDesc(providerId);
        } else if (status != null && !status.isBlank()) {
            bookings = bookingRepository.findByStatusOrderByScheduledTimeDesc(BookingStatus.valueOf(status.toUpperCase()));
        } else {
            bookings = bookingRepository.findAll();
        }
        return bookings.stream().map(this::mapBookingToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
        return mapBookingToDTO(booking);
    }

    @Override
    public BookingDTO updateBookingStatus(Long id, BookingStatusUpdateRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        if (request.getStatus() != null) {
            BookingStatus newStatus = BookingStatus.valueOf(request.getStatus().toUpperCase());
            booking.setStatus(newStatus);
        }
        if (request.getProviderId() != null) {
            User provider = userRepository.findById(request.getProviderId())
                    .orElseThrow(() -> new RuntimeException("Provider not found with ID: " + request.getProviderId()));
            booking.setProvider(provider);
        }
        if (request.getCancellationReason() != null) {
            booking.setCancellationReason(request.getCancellationReason());
        }

        Booking updated = bookingRepository.save(booking);
        return mapBookingToDTO(updated);
    }

    @Override
    public BookingDTO assignProvider(Long id, Long providerId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider user not found with ID: " + providerId));

        booking.setProvider(provider);
        booking.setStatus(BookingStatus.ACCEPTED);

        Booking updated = bookingRepository.save(booking);
        return mapBookingToDTO(updated);
    }

    @Override
    public void cancelBooking(Long id, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        bookingRepository.save(booking);
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
