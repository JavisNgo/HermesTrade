package com.ducnt.account.dto.response;

import com.ducnt.account.enums.AccountStatus;
import com.ducnt.account.model.Account;
import com.ducnt.account.model.AccountBalance;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreationResponse {
    String email;
    AccountStatus status;
    long availableBalance;
    String localCurrency;

    @Builder.Default
    String message = "Account activated successfully";

    public static UserCreationResponse fromAccount(Account account) {
        return UserCreationResponse.builder()
                .email(account.getEmail())
                .status(account.getStatus())
                .build();
    }

    public static UserCreationResponse fromAccountAndAccountBalance(Account account, AccountBalance accountBalance) {
        return UserCreationResponse.builder()
                .email(account.getEmail())
                .status(account.getStatus())
                .availableBalance(accountBalance.getAvailableBalance())
                .localCurrency(accountBalance.getCurrency())
                .build();
    }
}
