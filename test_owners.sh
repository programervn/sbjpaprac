#!/bin/bash

BASE_URL="http://localhost:8080/api"

echo "Creating an owner..."
OWNER_RESPONSE=$(curl -s -X POST "$BASE_URL/owners" \
  -H "Content-Type: application/json" \
  -d '{
    "firstname": "John",
    "lastname": "Doe"
  }')
echo "Owner Created: $OWNER_RESPONSE"
OWNER_ID=$(echo $OWNER_RESPONSE | jq -r '.ownerid')

if [ "$OWNER_ID" == "null" ]; then
  echo "Failed to create owner. Exiting."
  exit 1
fi

echo "Getting owner by ID: $OWNER_ID..."
curl -s -X GET "$BASE_URL/owners/$OWNER_ID" | jq .

echo "Creating a car for owner $OWNER_ID..."
CAR_RESPONSE=$(curl -s -X POST "$BASE_URL/cars" \
  -H "Content-Type: application/json" \
  -d "{
    \"brand\": \"Toyota\",
    \"model\": \"Camry\",
    \"color\": \"Silver\",
    \"registrationNumber\": \"OWNER-CAR-1\",
    \"modelYear\": 2023,
    \"price\": 30000.00,
    \"owner\": {
        \"ownerid\": $OWNER_ID
    }
  }")
echo "Car Created: $CAR_RESPONSE"

echo "Getting cars for owner $OWNER_ID..."
curl -s -X GET "$BASE_URL/owners/$OWNER_ID/cars" | jq .
