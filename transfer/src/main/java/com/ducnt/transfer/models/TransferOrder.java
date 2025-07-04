package com.ducnt.transfer.models;

import com.ducnt.transfer.dto.request.TradeRequest;
import com.ducnt.transfer.enums.Action;
import com.ducnt.transfer.enums.PaymentAction;
import com.ducnt.transfer.enums.ProcessStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@DynamoDbBean
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class TransferOrder {
    String pk;
    String sk;
    @Getter
    String externalRef;
    @Getter
    String accountId;
    @Getter
    String action;
    @Getter
    String paymentAction;
    @Getter
    String processStatus;
    @Getter
    String clientId;
    @Getter
    String requestType; // purpose
    @Getter
    String updateDate;
    @Getter
    String fromAccountId;
    @Getter
    String toAccountId;
    @Getter
    String amount;
    @Getter
    String createdAt;
    @Getter
    String completedAt;
    @Getter
    String failReason;
    @Getter
    Long ttl;

    @DynamoDbPartitionKey
    public String getPk() {
        return pk == null ? "TRANSFER#" + externalRef : pk;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk == null ? "METADATA#" + externalRef : sk;
    }

    public static TransferOrder onCreation(TradeRequest request, String externalRef) {
        return TransferOrder.builder()
                .fromAccountId(request.getFromAccountId())
                .toAccountId(request.getToAccountId())
                .requestType(request.getPurpose())
                .amount(String.valueOf(request.getAmount()))
                .createdAt(String.valueOf(LocalDate.now()))
                .externalRef(externalRef)
                .processStatus(String.valueOf(ProcessStatus.PENDING))
                .paymentAction(String.valueOf(PaymentAction.TRANSFER))
                .action(String.valueOf(Action.INITIATE))
                .build();
    }
}
