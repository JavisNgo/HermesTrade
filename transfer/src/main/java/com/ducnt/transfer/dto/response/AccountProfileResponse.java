package com.ducnt.transfer.dto.response;

import com.ducnt.transfer.enums.AccountStatus;
import com.ducnt.transfer.enums.ProfileType;
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
    String currency;
}
