package com.ducnt.account.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public enum ErrorResponse {

    EMAIL_INVALID("Email is invalid", "001"),
    PASSWORD_LENGTH_INVALID("Password Length Should Be At Least 8 Characters","002"),
    WEAK_PASSWORD("Weak Password", "003"),
    FULL_NAME_IS_REQUIRED("Full Name Is Required","004"),
    AGE_MUST_BE_AT_LEAST("Age Must Be At Least 18","005"),
    ADDRESS_IS_REQUIRED("Address Is Required","006"),
    EMAIL_ALREADY_EXISTS("Email Already Exists", "007"),
    ;

    private final String SERVICE_IDENTIFIER = "0601";

    private String message;
    private String errorCode;

    ErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = SERVICE_IDENTIFIER + errorCode;
    }
}
