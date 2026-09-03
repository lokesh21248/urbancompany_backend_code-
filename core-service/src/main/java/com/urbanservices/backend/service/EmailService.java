package com.urbanservices.backend.service;

import java.util.concurrent.CompletableFuture;

public interface EmailService {

    /**
     * Sends a transactional email asynchronously.
     *
     * @param to recipient email
     * @param subject subject line
     * @param htmlBody HTML formatted body
     * @return future completing with boolean status
     */
    CompletableFuture<Boolean> sendEmailAsync(String to, String subject, String htmlBody);

    /**
     * Sends an OTP verification email.
     */
    CompletableFuture<Boolean> sendOtpEmailAsync(String to, String otp);

    /**
     * Sends a booking status update email.
     */
    CompletableFuture<Boolean> sendBookingUpdateEmailAsync(String to, String bookingNumber, String statusMessage);
}
