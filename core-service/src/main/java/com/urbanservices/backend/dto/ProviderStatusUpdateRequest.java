package com.urbanservices.backend.dto;

public class ProviderStatusUpdateRequest {
    private Boolean isOnline;
    private String verificationStatus;

    public ProviderStatusUpdateRequest() {}

    public Boolean getIsOnline() { return isOnline; }
    public void setIsOnline(Boolean isOnline) { this.isOnline = isOnline; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
}
