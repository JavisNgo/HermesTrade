package com.ducnt.authentication.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public enum DomainCode {

    ACCOUNT_INCORRECT("Account Incorrect", "001"),
    ;

    private final String SERVICE_IDENTIFIER = "0602";

    private String message;
    private String errorCode;

    DomainCode(String message, String errorCode) {
        this.message = message;
        this.errorCode = SERVICE_IDENTIFIER + errorCode;
    }
}
