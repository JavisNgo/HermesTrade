package com.ducnt.transfer.models;

import com.ducnt.transfer.dto.request.Instruction;
import com.ducnt.transfer.dto.request.TradeRequest;
import com.ducnt.transfer.dto.response.AccountProfileResponse;
import com.ducnt.transfer.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

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
    String destinationClientId;
    @Getter
    String clientType;
    @Getter
    String destinationClientType;
    @Getter
    String asset;
    @Getter
    String availableBalance;
    @Getter
    String status;
    @Getter
    String updateDate;
    @Getter
    Long ttl;
    Long version;

    @Getter
    String gsiPk;
    @Getter
    String gsiSk;

    @DynamoDbPartitionKey
    public String getPk() {
        return pk == null ? "INTEGRATION#" + clientId + destinationClientId : pk;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk == null ? "METADATA#" + clientId + destinationClientId : sk;
    }

    @DynamoDbVersionAttribute
    public Long getVersion() {
        return version;
    }


    public static Integration onCreation(TradeRequest tradeRequest) {
        Integration integration = new Integration();
        for (Instruction instruction : tradeRequest.getInstructions()) {
            if (instruction.getSide().equals("CREDIT")) {
                integration.setDestinationClientId(instruction.getClientId());
                integration.setDestinationClientType(instruction.getType());
            } else if (instruction.getSide().equals("DEBIT")) {
                integration.setClientId(instruction.getClientId());
                integration.setClientType(instruction.getType());
                integration.setAvailableBalance(String.valueOf(tradeRequest.getAmount()));
            }
        }
        return integration;
    }

    public static Integration onCreationWithUniqueKey(TradeRequest tradeRequest, String externalRef) {
        Integration integration = onCreation(tradeRequest);
        integration.pk = "INTEGRATION#" + integration.getClientId() + integration.getDestinationClientId() + "_" + externalRef;
        integration.sk = "METADATA#" + externalRef;

        integration.gsiPk = "PAIR#" + integration.getClientId() + integration.getDestinationClientId();
        integration.gsiSk = externalRef;

        return integration;
    }

}
