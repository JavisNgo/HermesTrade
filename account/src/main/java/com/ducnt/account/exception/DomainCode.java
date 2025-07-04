package com.ducnt.account.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum DomainCode {
    ACCOUNT_NOT_FOUND("Account not found", "000"),
    EMAIL_INVALID("Email is invalid", "001"),
    PASSWORD_LENGTH_INVALID("Password Length Should Be At Least 8 Characters","002"),
    WEAK_PASSWORD("Weak Password", "003"),
    FULL_NAME_IS_REQUIRED("Full Name Is Required","004"),
    AGE_MUST_BE_AT_LEAST("Age Must Be At Least 18","005"),
    ADDRESS_IS_REQUIRED("Address Is Required","006"),
    EMAIL_ALREADY_EXISTS("Email Already Exists", "007"),
    PASSWORD_INCORRECT("Password Incorrect", "008"),
    UNEXPECTED_ERROR_CODE("Unexpected Error Code", "009"),
    SERVICE_UNAVAILABLE("Service Unavailable", "010"),
    ;

    private final String SERVICE_IDENTIFIER = "0601";

    private String message;
    private String errorCode;

    DomainCode(String message, String errorCode) {
        this.message = message;
        this.errorCode = SERVICE_IDENTIFIER + errorCode;
    }
}
