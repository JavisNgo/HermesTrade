package com.ducnt.distributedratelimit.service;

import com.ducnt.distributedratelimit.dto.request.RateLimitRequest;
import com.ducnt.distributedratelimit.exception.DomainCode;
import com.ducnt.distributedratelimit.exception.DomainException;
import com.ducnt.distributedratelimit.model.TokenRecord;
import com.ducnt.distributedratelimit.utils.Util;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimitService implements IRateLimitService {

    DynamoDbTable<TokenRecord> tokenRecordTable;

    static int DEFAULT_CAPACITY = 5;
    static int REFILL_RATE = 1/60;
    static int REQUEST_TOKEN = 1;

    @Override
    public boolean handelRateLimit(RateLimitRequest request) {

        String sortKey = request.getClientId() != null ? request.getClientId() : request.getIpAddress();

        Key key = Key.builder()
                .partitionValue(request.getServiceName())
                .sortValue(sortKey)
                .build();
        TokenRecord item = tokenRecordTable.getItem(key);

        if (item == null) {
            item = TokenRecord.onCreation(request, sortKey, DEFAULT_CAPACITY, REFILL_RATE);
        }

        long newToken = (request.getRequestTime() - item.getLastRefillTime()) * REFILL_RATE;

        long currentToken = Math.min(DEFAULT_CAPACITY, newToken + item.getCurrentTokens());
        try {
            if(currentToken >= REQUEST_TOKEN) {
                item.setCurrentTokens((int) (currentToken - REQUEST_TOKEN));
                item.setLastRefillTime(Util.getEpochTimeStamp());
                tokenRecordTable.updateItem(item);
                return true;
            } else {
                return false;
            }
        } catch (ConditionalCheckFailedException e) {
            log.warn( e.getMessage());
            throw new DomainException(DomainCode.REQUEST_CONFLICT);
        }
    }
}
