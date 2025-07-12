package com.ducnt.transfer.dto.request;

import com.ducnt.transfer.models.Integration;
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

    public static FinalizeMessageRequest onCreation(Integration integration) {
        return FinalizeMessageRequest.builder()
                .clientId(integration.getClientId())
                .destinationClientId(integration.getDestinationClientId())
                .amount(integration.getAvailableBalance())
                .build();
    }
}
