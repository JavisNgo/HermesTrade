package com.ducnt.account.dto.response.auth;

import com.ducnt.account.enums.AccountStatus;
import com.ducnt.account.model.Account;
import com.ducnt.account.model.AccountBalance;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreationResponse {
    UUID clientId;
    String email;
    AccountStatus status;
    BigDecimal availableBalance;
    String localCurrency;

    @Builder.Default
    String message = "Account activated successfully";

    public static UserCreationResponse fromAccount(Account account) {
        return UserCreationResponse.builder()
                .email(account.getEmail())
                .status(account.getStatus())
                .build();
    }

    public static UserCreationResponse onCreationSuccess(Account account, AccountBalance accountBalance) {
        return UserCreationResponse.builder()
                .clientId(account.getClientId())
                .email(account.getEmail())
                .status(account.getStatus())
                .availableBalance(accountBalance.getAvailableBalance())
                .localCurrency(accountBalance.getCurrency())
                .build();
    }
}
