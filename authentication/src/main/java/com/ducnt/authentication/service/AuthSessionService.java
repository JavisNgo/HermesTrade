package com.ducnt.authentication.service;

import com.ducnt.authentication.dto.request.LoginRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import utils.Util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthSessionService {
    DynamoDbClient dynamoDbClient;

    public String createAuthSession(LoginRequest loginRequest) {
        try {
            HashMap<String, AttributeValue> items = new HashMap<>();
            items.put("sessionId", AttributeValue.builder().s(UUID.randomUUID().toString()).build());

            //todo TTL phải là 30 phút
            items.put("ttl", AttributeValue.builder().n(Util.getEpochTimeStamp().toString()).build());

            PutItemRequest putItemRequest = PutItemRequest
                    .builder()
                    .tableName("auth_session")
                    .item(items)
                    .build();
            dynamoDbClient.putItem(putItemRequest);
            return "Success";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Fail";
        }
    }
}
