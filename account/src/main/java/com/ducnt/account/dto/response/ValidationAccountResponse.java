package com.ducnt.account.dto.response;

import com.ducnt.account.enums.AccountStatus;
import com.ducnt.account.model.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ValidationAccountResponse {
    UUID clientId;
    boolean status;

    public static ValidationAccountResponse formAccount(Account account) {
        return ValidationAccountResponse.builder()
                .clientId(account.getClientId())
                .status(account.getStatus() == AccountStatus.ACTIVE)
                .build();
    }
}
