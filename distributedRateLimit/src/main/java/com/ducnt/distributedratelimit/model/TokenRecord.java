package com.ducnt.distributedratelimit.model;

import com.ducnt.distributedratelimit.dto.request.RateLimitRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class TokenRecord {
    @Getter(onMethod = @__({@DynamoDbPartitionKey}))
    String serviceName;
    @Getter(onMethod = @__({@DynamoDbSortKey}))
    String clientId;
    @Getter
    int currentTokens;
    @Getter
    int capacity;
    @Getter
    Long lastRefillTime;
    @Getter
    int refillRate;
    @Getter(onMethod = @__({@DynamoDbVersionAttribute}))
    Long version;

    public static TokenRecord onCreation(RateLimitRequest rateLimitRequest, String clientId,
                                         int capacity, int refillRate) {
        return TokenRecord.builder()
                .clientId(clientId)
                .serviceName(rateLimitRequest.getServiceName())
                .capacity(capacity)
                .currentTokens(capacity)
                .refillRate(refillRate)
                .lastRefillTime(rateLimitRequest.getRequestTime())
                .build();
    }
}
