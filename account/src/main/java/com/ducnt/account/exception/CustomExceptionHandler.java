package com.ducnt.account.exception;

import com.ducnt.account.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestControllerAdvice
public class CustomExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleCustomException(CustomException e) {
        ErrorResponse errorResponse = e.getErrorResponse();
        var response = ResponseEntity.status(errorResponse.getStatus());
        return response.body(
                ApiResponse
                        .builder()
                        .code(errorResponse.getStatus().value())
                        .message(errorResponse.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentConversionNotSupportedException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.BAD_REQUEST;

        String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();

        Map<String, Object> attributes = null;
        try {
            errorResponse = ErrorResponse.valueOf(enumKey);
            Optional<ObjectError> objectError = e.getBindingResult().getAllErrors().stream().findFirst();
            if(objectError.isPresent()) {
                var constraintViolation = objectError.get().unwrap(ConstraintViolation.class);
                attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            }
        } catch (IllegalArgumentException ex) {

        }


        return ResponseEntity.status(errorResponse.getStatus())
                .body(ApiResponse
                        .builder()
                        .message(Objects.nonNull(attributes) ? attributes(errorResponse.getMessage(), attributes)
                                : Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                        .build());
    }

    private String attributes(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(CustomException e) {
        var response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.body(
                ApiResponse
                        .builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Internal server error!")
                        .build()
        );
    }
}
