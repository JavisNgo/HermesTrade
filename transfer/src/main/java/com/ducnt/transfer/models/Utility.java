package com.ducnt.transfer.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbAtomicCounter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Utility {
    String pk;
    String sk;
    Long counter;
    @Getter
    String lastUpdated;
    @Getter
    String idempotencyKey;
    @Getter
    Long ttl;

    @DynamoDbPartitionKey
    public String getPk() {
        return pk == null ? "UTILITY#" + idempotencyKey : pk;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk == null ? "METADATA#" + idempotencyKey : sk;
    }

    @DynamoDbAtomicCounter
    public Long getCounter() {
        return counter != null ? counter : 0L;
    }
}
