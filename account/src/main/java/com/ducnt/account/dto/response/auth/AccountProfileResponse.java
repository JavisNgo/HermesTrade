package com.ducnt.account.dto.response.auth;

import com.ducnt.account.enums.AccountStatus;
import com.ducnt.account.enums.ProfileType;
import com.ducnt.account.model.Account;
import com.ducnt.account.model.AccountBalance;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountProfileResponse {
    UUID clientId;
    ProfileType profileType;
    UUID accountNo;
    String fullName;
    BigDecimal availableBalance;
    BigDecimal actualBalance;
    AccountStatus status;

    public static AccountProfileResponse onCreationSuccess(Account account, AccountBalance accountBalance) {
        return AccountProfileResponse.builder()
                .clientId(account.getClientId())
                .accountNo(account.getId())
                .profileType(ProfileType.UNRESTRICTED) //Todo need function to check
                .fullName(account.getFullName())
                .availableBalance(accountBalance.getAvailableBalance())
                .actualBalance(accountBalance.getActualBalance())
                .status(account.getStatus())
                .build();
    }
}
