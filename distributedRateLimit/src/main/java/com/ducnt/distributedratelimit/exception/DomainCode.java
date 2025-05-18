package com.ducnt.distributedratelimit.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum DomainCode {

    RATE_LIMIT_EXCEEDED("Too many login attempts. Please try again later.", "001"),
    SERVICE_UNAVAILABLE("Service Unavailable", "002"),
    REQUEST_CONFLICT("Request conflict", "003"),
    ;

    private final String SERVICE_IDENTIFIER = "0603";

    private String message;
    private String errorCode;

    DomainCode(String message, String errorCode) {
        this.message = message;
        this.errorCode = SERVICE_IDENTIFIER + errorCode;
    }
}
