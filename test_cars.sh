#!/bin/bash

BASE_URL="http://localhost:8080/api/cars"

echo "1. Creating Toyota Camry (Black, 2023)..."
curl -s -X POST $BASE_URL \
-H "Content-Type: application/json" \
-d '{
    "brand": "Toyota",
    "model": "Camry",
    "color": "Black",
    "modelYear": 2023,
    "price": 30000.00
}' | jq .
echo -e "\n"

echo "2. Creating Toyota Corolla (White, 2022)..."
curl -s -X POST $BASE_URL \
-H "Content-Type: application/json" \
-d '{
    "brand": "Toyota",
    "model": "Corolla",
    "color": "White",
    "modelYear": 2022,
    "price": 25000.00
}' | jq .
echo -e "\n"

echo "3. Creating Honda Civic (Black, 2024)..."
curl -s -X POST $BASE_URL \
-H "Content-Type: application/json" \
-d '{
    "brand": "Honda",
    "model": "Civic",
    "color": "Black",
    "modelYear": 2024,
    "price": 28000.00
}' | jq .
echo -e "\n"

echo "--------------------------------------------------"

echo "4. Testing Get All Cars..."
curl -s -X GET $BASE_URL | jq .
echo -e "\n"

echo "5. Testing Get Cars by Brand (Toyota)..."
curl -s -X GET "$BASE_URL/brand/Toyota" | jq .
echo -e "\n"

echo "6. Testing Get Cars by Color (Black)..."
curl -s -X GET "$BASE_URL/color/Black" | jq .
echo -e "\n"

echo "7. Testing Get Cars by Year (2022)..."
curl -s -X GET "$BASE_URL/year/2022" | jq .
echo -e "\n"

echo "8. Testing Get Cars by Brand (Toyota) Sorted by Year..."
curl -s -X GET "$BASE_URL/brand/Toyota/sorted-by-year" | jq .
echo -e "\n"

echo "9. Testing Advanced Search (Generic Search: Toyota, Price > 20000)..."
curl -s -X GET "$BASE_URL/search?brand=Toyota&minPrice=20000&sort=price,desc" | jq .
echo -e "\n"
