package com.ducnt.authentication.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum DomainCode {

    ACCOUNT_INCORRECT("Account Incorrect", "001"),
    EMAIL_INCORRECT("Email Incorrect", "002"),
    RATE_LIMIT_EXCEEDED("Too many login attempts. Please try again later.", "003"),
    SERVICE_UNAVAILABLE("Service Unavailable", "004"),
    SESSION_EXPIRED("Session Expired", "005"),
    UNEXPECTED_ERROR_CODE("Unexpected Error Code", "006"),

    EMAIL_INVALID("Email is invalid", "0601001"),
    PASSWORD_LENGTH_INVALID("Password Length Should Be At Least 8 Characters","0601002"),
    WEAK_PASSWORD("Weak Password", "0601003"),
    FULL_NAME_IS_REQUIRED("Full Name Is Required","0601004"),
    AGE_MUST_BE_AT_LEAST("Age Must Be At Least 18","0601005"),
    ADDRESS_IS_REQUIRED("Address Is Required","0601006"),
    EMAIL_ALREADY_EXISTS("Email Already Exists", "0601007"),
    PASSWORD_INCORRECT("Password Incorrect", "0601008")
    ;

    private final String SERVICE_IDENTIFIER = "0602";

    private String message;
    private String errorCode;

    DomainCode(String message, String errorCode) {
        this.message = message;
        if(errorCode.length() < 3) {
            this.errorCode = SERVICE_IDENTIFIER + errorCode;
        } else {
            this.errorCode = errorCode;
        }
    }
}
