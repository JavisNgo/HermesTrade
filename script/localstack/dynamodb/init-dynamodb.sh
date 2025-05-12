#!/bin/bash

# -- > Create DynamoDb Table
echo Creating  DynamoDb \'ItemInfo\' table ...
# shellcheck disable=SC2046
# shellcheck disable=SC2005
echo $(awslocal dynamodb create-table --cli-input-json file:///table-schema.json --region us-east-1)

aws dynamodb update-time-to-live --table-name auth_session --time-to-live-specification "Enabled=true, AttributeName=ttl"

# --> List DynamoDb Tables
echo Listing tables ...
echo $(awslocal dynamodb list-tables)