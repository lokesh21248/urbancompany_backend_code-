package com.urbanservices.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BookingCreateRequest {
    private Long customerId;
    private Long addressId;
    private LocalDateTime scheduledTime;
    private List<ItemRequest> items;

    public static class ItemRequest {
        private Long serviceId;
        private Integer quantity;

        public ItemRequest() {}

        public Long getServiceId() { return serviceId; }
        public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public BookingCreateRequest() {}

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public List<ItemRequest> getItems() { return items; }
    public void setItems(List<ItemRequest> items) { this.items = items; }
}
