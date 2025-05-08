package com.ducnt.account.dto.response;

import com.ducnt.account.enums.AccountStatus;
import com.ducnt.account.model.Account;
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
    @Builder.Default
    String availableBalance = "0.00";
    @Builder.Default
    String localCurrency = "USD";
    @Builder.Default
    String message = "Account activated successfully";

    public static UserCreationResponse fromAccount(Account account) {
        return UserCreationResponse.builder()
                .email(account.getEmail())
                .status(account.getStatus())
                .build();
    }
}
