package com.ducnt.transfer.config;

import com.ducnt.transfer.models.Integration;
import com.ducnt.transfer.models.TransferOrder;
import com.ducnt.transfer.models.Utility;
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
    DynamoDbTable<Integration> integrationTable(DynamoDbClient dynamoDbClient,
                                                DynamoDbEnhancedClient enhancedClient) {
        DynamoDbTable<Integration> integrationTable = enhancedClient.table("integration",
                TableSchema.fromBean(Integration.class));
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName("integration").build());
        } catch (ResourceNotFoundException e) {
            integrationTable.createTable();
        }
        return integrationTable;
    }

    @Bean
    DynamoDbTable<TransferOrder> transferTable(DynamoDbClient dynamoDbClient,
                                               DynamoDbEnhancedClient enhancedClient) {
        DynamoDbTable<TransferOrder> transferTable = enhancedClient.table("transfer",
                TableSchema.fromBean(TransferOrder.class));
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName("transfer").build());
        } catch (ResourceNotFoundException e) {
            transferTable.createTable();
        }
        return transferTable;
    }

    @Bean
    DynamoDbTable<Utility> utilityTable(DynamoDbClient dynamoDbClient,
                                        DynamoDbEnhancedClient enhancedClient) {
        DynamoDbTable<Utility> utilityTable = enhancedClient.table("utility",
                TableSchema.fromBean(Utility.class));
        try{
            dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName("utility").build());
        }catch (ResourceNotFoundException e){
            utilityTable.createTable();
        }
        return utilityTable;
    }
}
