package com.ducnt.distributedratelimit.config;

import com.ducnt.distributedratelimit.model.TokenRecord;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Configuration
public class DynamoDbTableInitializer {

    @Bean
    DynamoDbTable<TokenRecord> tokenRecordTable(DynamoDbClient dynamoDbClient,
                                                DynamoDbEnhancedClient enhancedClient) {
        DynamoDbTable<TokenRecord> tokenRecordTable = enhancedClient.table("token_record",
                TableSchema.fromBean(TokenRecord.class));
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName("token_record").build());
        } catch (ResourceNotFoundException e) {
            tokenRecordTable.createTable();
        }
        return tokenRecordTable;
    }
}
