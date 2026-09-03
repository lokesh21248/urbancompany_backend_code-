package com.urbanservices.backend.service.impl;

import com.urbanservices.backend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Async("taskExecutor")
    @Override
    public CompletableFuture<Boolean> sendEmailAsync(String to, String subject, String htmlBody) {
        log.info("[EMAIL DISPATCH] To: {} | Subject: {}", to, subject);
        // Phase 28: Wire with Amazon SES / SMTP mailer
        return CompletableFuture.completedFuture(true);
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<Boolean> sendOtpEmailAsync(String to, String otp) {
        log.info("[OTP EMAIL] To: {} | Code: {}", to, otp);
        return CompletableFuture.completedFuture(true);
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<Boolean> sendBookingUpdateEmailAsync(String to, String bookingNumber, String statusMessage) {
        log.info("[BOOKING EMAIL] To: {} | Booking: {} | Message: {}", to, bookingNumber, statusMessage);
        return CompletableFuture.completedFuture(true);
    }
}
