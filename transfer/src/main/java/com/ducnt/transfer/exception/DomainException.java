package com.ducnt.transfer.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DomainException extends RuntimeException {
    DomainCode domainCode;
    HttpStatus httpStatus;

    public DomainException(DomainCode domainCode) {
        super(domainCode.getMessage());
        this.domainCode = domainCode;
    }

    public DomainException(DomainCode domainCode, HttpStatus httpStatus) {
        super(domainCode.getMessage());
        this.domainCode = domainCode;
        this.httpStatus = httpStatus;
    }
}
