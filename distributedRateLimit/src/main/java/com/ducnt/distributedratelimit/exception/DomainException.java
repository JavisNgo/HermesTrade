package com.ducnt.distributedratelimit.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DomainException extends RuntimeException {
    DomainCode domainCode;
    public DomainException(DomainCode domainCode) {
        super(domainCode.getMessage());
        this.domainCode = domainCode;
    }
}
