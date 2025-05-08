package com.ducnt.account.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorResponse {
    ACCOUNT_INVALID("Account is invalid", HttpStatus.BAD_REQUEST),

    BAD_REQUEST("Invalid request", HttpStatus.BAD_REQUEST),
    ;

    private String message;
    private HttpStatus status;
}
