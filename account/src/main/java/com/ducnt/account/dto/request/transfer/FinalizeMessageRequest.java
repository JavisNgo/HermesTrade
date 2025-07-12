package com.ducnt.account.dto.request.transfer;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults (level = AccessLevel.PRIVATE)
@Builder
public class FinalizeMessageRequest {
    String clientId;
    String destinationClientId;
    String amount;
}
