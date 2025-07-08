package com.ducnt.transfer.service;

import com.ducnt.transfer.clients.AccountClient;
import com.ducnt.transfer.dto.request.TradeRequest;
import com.ducnt.transfer.dto.response.AccountProfileResponse;
import com.ducnt.transfer.dto.response.TransferResponse;
import com.ducnt.transfer.enums.Action;
import com.ducnt.transfer.exception.DomainCode;
import com.ducnt.transfer.exception.DomainException;
import com.ducnt.transfer.models.Integration;
import com.ducnt.transfer.models.TransferOrder;
import com.ducnt.transfer.models.Utility;
import com.ducnt.transfer.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransferService implements ITransferService {
    static long SESSION_TTL_SECONDS = 1800L;

    @Value("${kafka.topic.name}")
    @NonFinal
    String accountTopic;

    DynamoDbTable<Integration> integrationTable;
    DynamoDbTable<TransferOrder> transferOrderTable;
    DynamoDbTable<Utility> utilityTable;
    AccountClient accountClient;
    ObjectMapper objectMapper;
    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public TransferResponse reserve(TradeRequest tradeRequest, String idempotencyKey) {
        String externalRef = Util.generateExternalRef(tradeRequest.getTimestamp());

        AccountProfileResponse fromAccount = getAccountProfile(tradeRequest.getFromAccountId());
        AccountProfileResponse toAccount = getAccountProfile(tradeRequest.getToAccountId());

        if(fromAccount == null || toAccount == null) {
            throw new DomainException(DomainCode.SERVICE_UNAVAILABLE,HttpStatus.SERVICE_UNAVAILABLE);
        }

        Integration fromAccountIntegration = Integration.onCreation(fromAccount, externalRef);
        Integration toAccountIntegration = Integration.onCreation(toAccount, externalRef);

        BigDecimal newAvailableBalance = fromAccount.getAvailableBalance().subtract(tradeRequest.getAmount());

        if (newAvailableBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException(DomainCode.INSUFFICIENT_BALANCE,HttpStatus.BAD_REQUEST);
        }

        fromAccountIntegration.setCurrentBalance(newAvailableBalance.toString());

        TransferOrder transferOrder = TransferOrder.onCreation(tradeRequest, externalRef);
        transferOrder.setAction(String.valueOf(Action.REVERSE));

        Utility utility = new Utility();
        utility.setIdempotencyKey(idempotencyKey);

        Long ttl = Util.getEpochTimeStamp() + SESSION_TTL_SECONDS;
        fromAccountIntegration.setTtl(ttl);
        toAccountIntegration.setTtl(ttl);
        transferOrder.setTtl(ttl);

        try {
            Expression expression = Expression.builder()
                    .expression("attribute_not_exists(pk)")
                    .build();
            utilityTable.putItem(r -> r.item(utility).conditionExpression(expression));
            integrationTable.putItem(r ->
                    r.item(fromAccountIntegration).conditionExpression(expression));
            transferOrderTable.putItem(r ->
                    r.item(transferOrder).conditionExpression(expression));
        } catch (ConditionalCheckFailedException e) {
            throw new DomainException(DomainCode.DATABASE_ERROR, HttpStatus.BAD_REQUEST);
        }

        kafkaTemplate.send(accountTopic, 1,
                fromAccount.getClientId().toString(), "RESERVE#" + newAvailableBalance.toString());
        return TransferResponse.onCreation(transferOrder, idempotencyKey);
    }

    private AccountProfileResponse getAccountProfile(String accountId) {
        ResponseEntity<AccountProfileResponse> response = null;
        try {
            response = accountClient.validateAccount(accountId);
        } catch (FeignException.FeignClientException e) {
            try {
                String errorBody = e.responseBody().map(bb -> new String(bb.array())).orElse("");
                DomainCode errorResponse = objectMapper.readValue(errorBody, DomainCode.class);
                String errorCode = errorResponse.getErrorCode();

                Optional<DomainCode> domainCode = Arrays.stream(DomainCode.values())
                        .filter(dc -> dc.getErrorCode().equals(errorCode))
                        .findFirst();

                if (domainCode.isPresent()) {
                    throw new DomainException(domainCode.get(), HttpStatus.BAD_REQUEST);
                } else {
                    throw new DomainException(DomainCode.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
                }

            } catch (Exception ignored) {
                throw new DomainException(DomainCode.UNEXPECTED_ERROR_CODE, HttpStatus.BAD_REQUEST);
            }
        }

        if (response == null) {
            throw new DomainException(DomainCode.SERVICE_UNAVAILABLE,HttpStatus.SERVICE_UNAVAILABLE);
        }

        return response.getBody();
    }


}
