package com.ducnt.authentication.service;

import com.ducnt.authentication.clients.AccountClient;
import com.ducnt.authentication.dto.request.LoginRequest;
import com.ducnt.authentication.dto.response.AuthSessionResponse;
import com.ducnt.authentication.dto.response.ErrorResponse;
import com.ducnt.authentication.enums.AccountStatus;
import com.ducnt.authentication.exception.DomainCode;
import com.ducnt.authentication.exception.DomainException;
import com.ducnt.authentication.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthSessionService {
    DynamoDbClient dynamoDbClient;
    AccountClient accountClient;
    ObjectMapper objectMapper;

    static long SESSION_TTL_SECONDS = 1800L;

    public AuthSessionResponse authenticate(LoginRequest loginRequest) {
        ResponseEntity<AuthSessionResponse> accountResponse = null;
        try {
            accountResponse = accountClient.validateAccount(loginRequest);
        } catch (FeignException.FeignClientException e) {
            if (e.status() == HttpStatus.BAD_REQUEST.value()) {
                try {
                    String errorBody = e.responseBody().map(bb -> new String(bb.array())).orElse("");
                    ErrorResponse errorResponse = objectMapper.readValue(errorBody, ErrorResponse.class);
                    String errorCode = errorResponse.getErrorCode();

                    Optional<DomainCode> domainCode = Arrays.stream(DomainCode.values())
                            .filter(dc -> dc.getErrorCode().equals(errorCode))
                            .findFirst();

                    if (domainCode.isPresent()) {
                        throw new DomainException(domainCode.get(), HttpStatus.BAD_REQUEST);
                    } else {
                        throw new DomainException(DomainCode.UNEXPECTED_ERROR_CODE, HttpStatus.BAD_REQUEST);
                    }

                } catch (Exception ignored) {
                    throw new DomainException(DomainCode.UNEXPECTED_ERROR_CODE, HttpStatus.BAD_REQUEST);
                }
            } else if (e.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                throw new DomainException(DomainCode.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
            }
        }

        if(accountResponse == null) throw new DomainException(DomainCode.SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
        AuthSessionResponse authSessionResponse = accountResponse.getBody();

        if(authSessionResponse == null) throw new DomainException(DomainCode.SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
        String clientId = String.valueOf(authSessionResponse.getClientId());

        try {
            Map<String, AttributeValue> authSession = createAuthSession(clientId);
            return AuthSessionResponse.fromItem(authSession);
        } catch (ConditionalCheckFailedException e) {
            Map<String, AttributeValue> authSession = getAuthSession(clientId);
            return AuthSessionResponse.fromItem(authSession);
        }
    }

    public Map<String, AttributeValue> createAuthSession(String clientId) {

        HashMap<String, AttributeValue> items = new HashMap<>();

        items.put("clientId", AttributeValue.builder().s(clientId).build());

        UUID sessionId = UUID.randomUUID();
        items.put("sessionId", AttributeValue.builder().s(String.valueOf(sessionId)).build());

        long ttlEpochTime = Util.getEpochTimeStamp() + SESSION_TTL_SECONDS;
        items.put("ttl", AttributeValue.builder().n(Long.toString(ttlEpochTime)).build());

        items.put("status", AttributeValue.builder().s(String.valueOf(AccountStatus.ACTIVE)).build());

        PutItemRequest putItemRequest = PutItemRequest
                .builder()
                .tableName("auth_session")
                .conditionExpression("attribute_not_exists(clientId) AND attribute_not_exists(sessionId)")
                .item(items)
                .build();
        dynamoDbClient.putItem(putItemRequest);
        return items;
    }

    public Map<String,AttributeValue> getAuthSession(String clientId) {
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();

        keyToGet.put("clientId", AttributeValue.builder().s(clientId).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("auth_session")
                .key(keyToGet)
                .build();

        return dynamoDbClient.getItem(getItemRequest).item();
    }

    public void logout(String clientId, String sessionId) {
        HashMap<String, AttributeValue> key = new HashMap<>();
        key.put("clientId", AttributeValue.builder().s(clientId).build());

        String conditionExpression = "sessionId = :sessionId";
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":sessionId", AttributeValue.builder().s(sessionId).build());
        expressionAttributeValues.put(":status", AttributeValue.builder().s(String.valueOf(AccountStatus.INACTIVE)).build());

        String updateExpression = "SET #status = :status";
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#status", "status");

        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName("auth_session")
                .key(key)
                .updateExpression(updateExpression)
                .expressionAttributeNames(expressionAttributeNames)
                .expressionAttributeValues(expressionAttributeValues)
                .conditionExpression(conditionExpression)
                .build();

        try {
            dynamoDbClient.updateItem(updateItemRequest);
        } catch (ConditionalCheckFailedException e) {
            throw new DomainException(DomainCode.SESSION_EXPIRED, HttpStatus.BAD_REQUEST);
        }
    }

}
