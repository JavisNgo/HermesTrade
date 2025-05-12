package com.ducnt.authentication.service;

import com.ducnt.authentication.clients.AccountClient;
import com.ducnt.authentication.dto.request.LoginRequest;
import com.ducnt.authentication.dto.response.ValidationAccountResponse;
import com.ducnt.authentication.exception.DomainEnumException;
import com.ducnt.authentication.exception.DomainException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import utils.Util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthSessionService {
    DynamoDbClient dynamoDbClient;
    AccountClient accountClient;

    public ValidationAccountResponse login(LoginRequest loginRequest, String sessionId, String clientId) {
        Map<String, AttributeValue> items = getAuthSession(sessionId, clientId);

        if(!items.isEmpty()) {
            return ValidationAccountResponse.fromItem(items);
        }

        ResponseEntity<ValidationAccountResponse> accountResponse = accountClient
                .validateAccount(loginRequest);

        ValidationAccountResponse validationAccountResponse = accountResponse.getBody();

        if(validationAccountResponse == null) {
            throw new DomainException(DomainEnumException.ACCOUNT_INCORRECT);
        }

        createAuthSession(validationAccountResponse);

        return validationAccountResponse;
    }


    public Map<String,AttributeValue> getAuthSession(String sessionId, String clientId) {
        HashMap<String, AttributeValue> keyToGet = new HashMap<>();

        keyToGet.put("sessionId", AttributeValue.builder().s(sessionId).build());
        //keyToGet.put("clientId", AttributeValue.builder().s(clientId).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("auth_session")
                .key(keyToGet)
                .build();

        return dynamoDbClient.getItem(getItemRequest).item();
    }

    public void createAuthSession(ValidationAccountResponse validationAccountResponse) {

        HashMap<String, AttributeValue> items = new HashMap<>();

        UUID sessionId = UUID.randomUUID();
        items.put("sessionId", AttributeValue.builder().s(sessionId.toString()).build());

        long ttlEpochTime = Util.getEpochTimeStamp() + 1800L;
        items.put("ttl", AttributeValue.builder().n(Long.toString(ttlEpochTime)).build());

        items.put("clientId", AttributeValue.builder().s(validationAccountResponse.getClientId().toString()).build());

        items.put("status", AttributeValue.builder().bool(validationAccountResponse.isStatus()).build());

        PutItemRequest putItemRequest = PutItemRequest
                .builder()
                .tableName("auth_session")
                .item(items)
                .build();
        dynamoDbClient.putItem(putItemRequest);

        validationAccountResponse.setSessionId(sessionId);
    }

}
