package com.urbanservices.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardStatsDTO {
    private BigDecimal totalRevenue;
    private Long totalBookings;
    private Long activeBookings;
    private Long completedBookings;
    private Long totalProviders;
    private Long onlineProviders;
    private Long totalCustomers;
    private List<BookingDTO> recentBookings;

    public DashboardStatsDTO() {}

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public Long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(Long totalBookings) { this.totalBookings = totalBookings; }

    public Long getActiveBookings() { return activeBookings; }
    public void setActiveBookings(Long activeBookings) { this.activeBookings = activeBookings; }

    public Long getCompletedBookings() { return completedBookings; }
    public void setCompletedBookings(Long completedBookings) { this.completedBookings = completedBookings; }

    public Long getTotalProviders() { return totalProviders; }
    public void setTotalProviders(Long totalProviders) { this.totalProviders = totalProviders; }

    public Long getOnlineProviders() { return onlineProviders; }
    public void setOnlineProviders(Long onlineProviders) { this.onlineProviders = onlineProviders; }

    public Long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(Long totalCustomers) { this.totalCustomers = totalCustomers; }

    public List<BookingDTO> getRecentBookings() { return recentBookings; }
    public void setRecentBookings(List<BookingDTO> recentBookings) { this.recentBookings = recentBookings; }
}
