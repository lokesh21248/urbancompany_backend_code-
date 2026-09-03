package com.urbanservices.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an authenticated user attempts an action they are not permitted to perform.
 * Results in HTTP 403 Forbidden.
 *
 * <p>Critical for ownership checks:
 * Customer A must never access Customer B's addresses, bookings, or profile.
 * This exception is thrown at the service layer after validating resource ownership.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("You do not have permission to perform this action");
    }
}
