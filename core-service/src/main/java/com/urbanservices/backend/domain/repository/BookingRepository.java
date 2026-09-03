package com.urbanservices.backend.domain.repository;

import com.urbanservices.backend.domain.entity.Booking;
import com.urbanservices.backend.domain.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerIdOrderByScheduledTimeDesc(Long customerId);
    List<Booking> findByProviderIdOrderByScheduledTimeDesc(Long providerId);
    List<Booking> findByStatusOrderByScheduledTimeDesc(BookingStatus status);
    Long countByStatus(BookingStatus status);

    @Query("SELECT DISTINCT b FROM Booking b JOIN b.items bi WHERE b.status = 'PENDING' AND b.provider IS NULL AND bi.serviceItemId IN :serviceIds ORDER BY b.createdAt DESC")
    List<Booking> findPendingBookingsByServiceIds(@Param("serviceIds") List<Long> serviceIds);
}
