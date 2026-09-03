package com.urbanservices.backend.service;

import com.urbanservices.backend.dto.BookingCreateRequest;
import com.urbanservices.backend.dto.BookingDTO;
import com.urbanservices.backend.dto.BookingStatusUpdateRequest;

import java.util.List;

public interface BookingService {
    BookingDTO createBooking(BookingCreateRequest request);
    List<BookingDTO> getAllBookings(String status, Long customerId, Long providerId);
    BookingDTO getBookingById(Long id);
    BookingDTO updateBookingStatus(Long id, BookingStatusUpdateRequest request);
    BookingDTO assignProvider(Long id, Long providerId);
    void cancelBooking(Long id, String reason);
}
