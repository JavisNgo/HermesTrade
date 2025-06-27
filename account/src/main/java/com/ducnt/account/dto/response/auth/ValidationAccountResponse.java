package com.ducnt.account.dto.response.auth;

import com.ducnt.account.enums.AccountStatus;
import com.ducnt.account.model.Account;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class ValidationAccountResponse {
    UUID clientId;
    AccountStatus status;

    public static ValidationAccountResponse formAccount(Account account) {
        return ValidationAccountResponse.builder()
                .clientId(account.getClientId())
                .status(account.getStatus())
                .build();
    }
}
