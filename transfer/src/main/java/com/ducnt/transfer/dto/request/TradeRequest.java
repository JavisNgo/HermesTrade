package com.ducnt.transfer.dto.request;

import com.ducnt.transfer.utils.Util;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeRequest {
    String paymentRef;
    String externalRef;
    String purpose;
    BigDecimal amount;
    Long timestamp = Util.getEpochTimeStamp();
    List<Instruction> instructions;
}
