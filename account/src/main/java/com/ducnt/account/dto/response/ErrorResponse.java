package com.ducnt.account.dto.response;

import com.ducnt.account.exception.DomainEnumException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;

    public static ErrorResponse fromDomainEnumResponse(DomainEnumException domainEnumException) {

        return ErrorResponse.builder()
                .errorCode(domainEnumException.getErrorCode())
                .errorMessage(domainEnumException.getMessage())
                .build();
    }
}
