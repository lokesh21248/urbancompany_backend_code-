package com.urbanservices.backend.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    /**
     * Sends an in-app or push notification asynchronously to a user.
     *
     * @param userId recipient user ID
     * @param title notification title
     * @param body notification body text
     * @param data custom key-value payload (e.g. screen navigation targets)
     * @return future completing with boolean status
     */
    CompletableFuture<Boolean> sendPushNotificationAsync(Long userId, String title, String body, Map<String, String> data);

    /**
     * Broadcasts a notification to all service providers in a given radius/category.
     */
    CompletableFuture<Boolean> broadcastToProvidersAsync(String categorySlug, String title, String body, Map<String, String> data);
}
