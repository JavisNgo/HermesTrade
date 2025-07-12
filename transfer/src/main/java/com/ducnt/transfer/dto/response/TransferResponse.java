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
    String amount;
    String asset;
    String processStatus;
    String paymentAction;
    String requestType;
    String idempotencyKey;
    String paymentRef;
    
    public static TransferResponse onCreation(TransferOrder transferOrder, String idempotencyKey) {
        return TransferResponse.builder()
                .externalRef(transferOrder.getExternalRef())
                .amount(transferOrder.getAmount())
                .asset(String.valueOf(Asset.VND))
                .processStatus(transferOrder.getProcessStatus())
                .paymentAction(transferOrder.getPaymentAction())
                .requestType(transferOrder.getRequestType())
                .idempotencyKey(idempotencyKey)
                .build();
    }

    public static TransferResponse onReservePaymentRef(String reservePaymentRef) {
        return TransferResponse.builder()
                .paymentRef(reservePaymentRef)
                .build();
    }
    public  static TransferResponse onFinalizePaymentRef(String finalizePaymentRef) {
        return TransferResponse.builder()
                .paymentRef(finalizePaymentRef)
                .build();
    }
}