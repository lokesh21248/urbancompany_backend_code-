package com.urbanservices.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a request requires authentication but none is present or valid.
 * Results in HTTP 401 Unauthorized.
 *
 * <p>This will be used extensively once Firebase Authentication is wired in (Phase 27).
 * For now it serves as the architectural placeholder and can be thrown
 * by middleware stubs during development.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("Authentication required");
    }
}
