package com.ducnt.transfer.dto.request;

import com.ducnt.transfer.utils.Util;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeRequest {
    String purpose;
    BigDecimal amount;
    Long timestamp = Util.getEpochTimeStamp();
    String fromAccountId;
    String toAccountId;
}
