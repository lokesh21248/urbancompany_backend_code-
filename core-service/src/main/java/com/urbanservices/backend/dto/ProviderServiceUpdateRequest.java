package com.urbanservices.backend.dto;

import java.util.List;

public class ProviderServiceUpdateRequest {
    private List<Long> serviceIds;

    public ProviderServiceUpdateRequest() {}

    public List<Long> getServiceIds() { return serviceIds; }
    public void setServiceIds(List<Long> serviceIds) { this.serviceIds = serviceIds; }
}
