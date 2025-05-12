package com.ducnt.authentication.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ValidationAccountResponse {
    UUID clientId;
    UUID sessionId;
    boolean status;

    public static ValidationAccountResponse fromItem(Map<String, AttributeValue> items) {
        return ValidationAccountResponse.builder()
                .clientId(UUID.fromString(items.get("clientId").s()))
                .sessionId(UUID.fromString(items.get("sessionId").s()))
                .status(items.get("status").bool())
                .build();
    }
}
