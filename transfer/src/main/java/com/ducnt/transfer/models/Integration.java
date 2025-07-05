package com.ducnt.transfer.models;

import com.ducnt.transfer.dto.response.AccountProfileResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

@DynamoDbBean
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class Integration {
    String pk;
    String sk;
    @Getter
    String clientId;
    @Getter
    String asset;
    @Getter
    String actualBalance;
    @Getter
    String currentBalance;
    @Getter
    String status;
    @Getter
    String externalRef;
    @Getter
    String updateDate;
    @Getter
    Long ttl;
    Long version;

    @DynamoDbPartitionKey
    public String getPk() {
        return pk == null ? "INTEGRATION#" + clientId : pk;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk == null ? "METADATA#" + clientId : sk;
    }

    @DynamoDbVersionAttribute
    public Long getVersion() {
        return version;
    }

    public static Integration onCreation(AccountProfileResponse account, String externalRef) {
        return Integration.builder()
                .clientId(String.valueOf(account.getClientId()))
                .asset(account.getCurrency())
                .actualBalance(String.valueOf(account.getActualBalance()))
                .currentBalance(String.valueOf(account.getAvailableBalance()))
                .status(String.valueOf(account.getStatus()))
                .externalRef(externalRef)
                .build();
    }

}
