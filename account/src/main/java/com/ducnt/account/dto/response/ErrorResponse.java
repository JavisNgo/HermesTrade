package com.ducnt.account.dto.response;

import com.ducnt.account.exception.DomainCode;
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

    public static ErrorResponse fromDomainEnumResponse(DomainCode domainCode) {
        return ErrorResponse.builder()
                .errorCode(domainCode.getErrorCode())
                .errorMessage(domainCode.getMessage())
                .build();
    }
}
