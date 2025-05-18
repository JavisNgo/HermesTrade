package com.ducnt.distributedratelimit.exception;

import com.ducnt.distributedratelimit.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DomainException.class)
    public ResponseEntity<com.ducnt.distributedratelimit.dto.response.ErrorResponse> handleDomainException(DomainException e) {
        ErrorResponse errorResponse = ErrorResponse.fromDomainEnumResponse(e.getDomainCode());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

}
