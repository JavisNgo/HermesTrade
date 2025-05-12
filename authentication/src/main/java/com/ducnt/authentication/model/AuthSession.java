//package com.ducnt.authentication.model;
//
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
//import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@DynamoDbBean
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class AuthSession {
//    UUID sessionId;
//    LocalDateTime expiresAt;
//
//    @DynamoDbPartitionKey
//    public UUID getSessionId() {
//        return sessionId;
//    }
//}
