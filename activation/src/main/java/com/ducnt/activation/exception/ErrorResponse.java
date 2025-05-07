package com.ducnt.activation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorResponse {
    USERNAME_EXISTS("Email already exists ", HttpStatus.CONFLICT),

    BAD_REQUEST("Invalid request", HttpStatus.BAD_REQUEST),
    ;

    private String message;
    private HttpStatus status;
}
