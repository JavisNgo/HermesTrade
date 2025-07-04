package com.ducnt.transfer.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum DomainCode {

    SERVICE_UNAVAILABLE("Service Unavailable", "001"),
    SESSION_EXPIRED("Session Expired", "002"),
    UNEXPECTED_ERROR_CODE("Unexpected Error Code", "003"),
    DATABASE_ERROR("Database Error", "004"),
    INSUFFICIENT_BALANCE("Insufficient Balance", "005"),


    EMAIL_INVALID("Email is invalid", "0601001"),
    PASSWORD_LENGTH_INVALID("Password Length Should Be At Least 8 Characters","0601002"),
    WEAK_PASSWORD("Weak Password", "0601003"),
    FULL_NAME_IS_REQUIRED("Full Name Is Required","0601004"),
    AGE_MUST_BE_AT_LEAST("Age Must Be At Least 18","0601005"),
    ADDRESS_IS_REQUIRED("Address Is Required","0601006"),
    EMAIL_ALREADY_EXISTS("Email Already Exists", "0601007"),
    PASSWORD_INCORRECT("Password Incorrect", "0601008")
    ;

    private final String SERVICE_IDENTIFIER = "0604";

    private String message;
    private String errorCode;

    DomainCode(String message, String errorCode) {
        this.message = message;
        this.errorCode = SERVICE_IDENTIFIER + errorCode;
    }
}
