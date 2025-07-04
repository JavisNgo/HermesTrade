package com.ducnt.transfer.dto.response;

import com.ducnt.transfer.enums.Asset;
import com.ducnt.transfer.models.Integration;
import com.ducnt.transfer.models.TransferOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferResponse {
    String externalRef;
    String fromAccountId;
    String toAccountId;
    String amount;
    String asset;
    String processStatus;
    String paymentAction;
    String requestType;
    String createdAt;
    String completedAt;
    String failReason;
    String idempotencyKey;
    
    public static TransferResponse onCreation(TransferOrder transferOrder, String idempotencyKey) {
        return TransferResponse.builder()
                .externalRef(transferOrder.getExternalRef())
                .fromAccountId(transferOrder.getFromAccountId())
                .toAccountId(transferOrder.getToAccountId())
                .amount(transferOrder.getAmount())
                .asset(String.valueOf(Asset.VND))
                .processStatus(transferOrder.getProcessStatus())
                .paymentAction(transferOrder.getPaymentAction())
                .requestType(transferOrder.getRequestType())
                .createdAt(transferOrder.getCreatedAt())
                .completedAt(transferOrder.getCompletedAt())
                .failReason(transferOrder.getFailReason())
                .idempotencyKey(idempotencyKey)
                .build();
    }
}