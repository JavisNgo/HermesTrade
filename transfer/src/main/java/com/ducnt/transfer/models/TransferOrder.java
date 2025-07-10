package com.ducnt.transfer.models;

import com.ducnt.transfer.dto.request.TradeRequest;
import com.ducnt.transfer.enums.PaymentAction;
import com.ducnt.transfer.enums.ProcessStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDate;

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
    String clientId;
    @Getter
    String action;
    @Getter
    String paymentAction;
    @Getter
    String processStatus;
    @Getter
    String requestType; // purpose
    @Getter
    String updateDate;
    @Getter
    String amount;
    String reservePaymentRef;
    @Getter
    String finalizePaymentRef;
    @Getter
    String createdAt;
    @Getter
    String completedAt;
    @Getter
    Long ttl;
    Long version;


    @DynamoDbPartitionKey
    public String getPk() {
        return pk == null ? "TRANSFER#" + externalRef : pk;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk == null ? "METADATA#" + externalRef : sk;
    }

    public String getReservePaymentRef() {
        return reservePaymentRef == null ? "PAY#" + externalRef : reservePaymentRef;
    }

    @DynamoDbVersionAttribute
    public Long getVersion() {
        return version;
    }

    public static TransferOrder onCreation(TradeRequest request, String externalRef) {
        return TransferOrder.builder()
                .externalRef(externalRef)
                .requestType(request.getPurpose())
                .amount(String.valueOf(request.getAmount()))
                .createdAt(String.valueOf(LocalDate.now()))
                .externalRef(externalRef)
                .processStatus(String.valueOf(ProcessStatus.PENDING))
                .paymentAction(String.valueOf(PaymentAction.TRANSFER))
                .build();
    }
}
