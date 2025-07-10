package com.ducnt.transfer.dto.request;

import com.ducnt.transfer.utils.Util;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeRequest {
    String purpose;
    BigDecimal amount;
    Long timestamp = Util.getEpochTimeStamp();
    List<Instruction> instructions;
}
