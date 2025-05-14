package com.ducnt.authentication.service;

import com.ducnt.authentication.clients.AccountClient;
import com.ducnt.authentication.dto.request.LoginRequest;
import com.ducnt.authentication.dto.response.ValidationAccountResponse;
import com.ducnt.authentication.exception.DomainCode;
import com.ducnt.authentication.exception.DomainException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import com.ducnt.authentication.utils.Util;

import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthSessionService {
    DynamoDbClient dynamoDbClient;
    AccountClient accountClient;

    static long SESSION_TTL_SECONDS = 1800L;

    public ValidationAccountResponse authenticate(LoginRequest loginRequest) {
        ResponseEntity<ValidationAccountResponse> accountResponse = accountClient
                .validateAccount(loginRequest);
        ValidationAccountResponse validationAccountResponse = accountResponse.getBody();

        if(validationAccountResponse == null) {
            throw new DomainException(DomainCode.ACCOUNT_INCORRECT);
        }

        String clientId = String.valueOf(validationAccountResponse.getClientId());

        List<Map<String, AttributeValue>> authSession = getAuthSession(clientId);

        if(!authSession.isEmpty()) {
            return ValidationAccountResponse.fromItem(authSession.get(0));
        }else {
            Map<String, AttributeValue> createAuthSession = createAuthSession(clientId);
            return ValidationAccountResponse.fromItem(createAuthSession);
        }
    }



    public Map<String, AttributeValue> createAuthSession(String clientId) {

        HashMap<String, AttributeValue> items = new HashMap<>();

        UUID sessionId = UUID.randomUUID();
        items.put("sessionId", AttributeValue.builder().s(String.valueOf(sessionId)).build());

        long ttlEpochTime = Util.getEpochTimeStamp() + SESSION_TTL_SECONDS;
        items.put("ttl", AttributeValue.builder().n(Long.toString(ttlEpochTime)).build());

        items.put("clientId", AttributeValue.builder().s(clientId).build());

        PutItemRequest putItemRequest = PutItemRequest
                .builder()
                .tableName("auth_session")
                .item(items)
                .build();
        dynamoDbClient.putItem(putItemRequest);
        return items;
    }

    public List<Map<String,AttributeValue>> getAuthSession(String clientId) {
        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#clientId", "clientId");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":clientId", AttributeValue.builder().s(clientId).build());

        QueryRequest queryRequest = QueryRequest.builder()
                .tableName("auth_session")
                .indexName("ClientIdIndex")
                .keyConditionExpression("#clientId = :clientId")
                .expressionAttributeNames(expressionAttributeNames)
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        return dynamoDbClient.query(queryRequest).items();
    }

}
