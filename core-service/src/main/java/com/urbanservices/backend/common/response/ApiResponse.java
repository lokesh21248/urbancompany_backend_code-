package com.urbanservices.backend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

/**
 * Standard API response envelope for all Urban Services REST endpoints.
 *
 * <p>All controllers must wrap their responses in this class.
 * Flutter clients parse this envelope to handle success and error uniformly.
 *
 * <p>Structure:
 * <pre>
 * {
 *   "success": true,
 *   "message": "Operation completed",
 *   "data": { ... },          // present on success, null on error
 *   "errors": { ... },        // present on validation failure, null on success
 *   "timestamp": "2024-01-01T00:00:00Z"
 * }
 * </pre>
 *
 * @param <T> The type of the response data payload
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final Map<String, String> errors;

    @Builder.Default
    private final Instant timestamp = Instant.now();

    // ── Static factory methods ────────────────────────────────────────────

    /**
     * Creates a successful response with data payload.
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .build();
    }

    /**
     * Creates a successful response with a custom message and data payload.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Creates a successful response with only a message (no data).
     * Useful for delete/action endpoints.
     */
    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .build();
    }

    /**
     * Creates an error response with a message.
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    /**
     * Creates a validation error response with field-level error details.
     */
    public static <T> ApiResponse<T> validationError(String message, Map<String, String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .build();
    }
}
