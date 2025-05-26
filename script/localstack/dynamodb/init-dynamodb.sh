#!/bin/bash

# shellcheck disable=SC2046
# shellcheck disable=SC2005
# -- > Create DynamoDb Table
echo Creating  DynamoDb \'auth_session\' table ...
echo $(awslocal dynamodb create-table --cli-input-json file:///table-schema.json --region us-east-1)
echo $(awslocal dynamodb update-time-to-live --table-name auth_session --time-to-live-specification "Enabled=true, AttributeName=ttl" --endpoint-url http://localhost:4566)
# --> List DynamoDb Tables
echo Listing tables ...
echo $(awslocal dynamodb list-tables)