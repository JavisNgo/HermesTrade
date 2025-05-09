package com.ducnt.account.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DomainException extends RuntimeException {
    DomainEnumException domainEnumException;
    public DomainException(DomainEnumException domainEnumException) {
        super(domainEnumException.getMessage());
        this.domainEnumException = domainEnumException;
    }
}
