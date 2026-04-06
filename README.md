# 🧾 Price API

Spring Boot application that exposes a REST endpoint to retrieve the applicable price for a product based on a given date, brand, and product ID.

---

## 🚀 Tech Stack

* Java 17+
* Spring Boot
* Spring Data JPA
* Springdoc OpenAPI
* H2 / Flyway
* JUnit 5 + Mockito

---

## 📌 Business Logic

The API returns the **applicable price** based on:

* Product ID
* Brand ID
* Application date

If multiple prices are valid for the given date, the one with the **highest priority** is selected.

---

## 🔗 Endpoint

### GET `/api/inditex/price`

#### Query parameters:

| Parameter  | Type          | Description                         |
| ---------- | ------------- | ----------------------------------- |
| product_id | Long          | Product identifier                  |
| brand_id   | Long          | Brand identifier                    |
| date       | LocalDateTime | Date when the price must be applied |

---

## 🧪 Example Request

```bash
curl -X GET "http://localhost:8888/api/inditex/price?product_id=35455&brand_id=1&date=2020-06-14T16:00:00"
```

---

## ✅ Example Response

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "curr": "EUR"
}
```

---

## 📚 API Documentation (Swagger)

This project uses Swagger via Springdoc OpenAPI. The easiest way to test the API.

### 👉 Access Swagger UI:

```
http://localhost:8888/swagger-ui/index.html
```

### 👉 OpenAPI JSON:

```
http://localhost:8888/v3/api-docs
```

---

## ▶️ How to Run

### 1. Clone repository

```bash
git clone <your-repo-url>
cd <your-project>
```

### 2. Run application

```bash
mvn spring-boot:run
```

Or from your IDE.

---

## 🧪 Running Tests

```bash
mvn test
```
---



## 🧠 Architecture

This project follows a **Clean Architecture / Hexagonal Architecture** approach:

* **Domain** → Core business logic (`Price`, `PriceResolver`)
* **Application** → Use cases (`FindPriceUseCase`)
* **Infrastructure** → Controllers, Persistence

---

## 👤 Author

* Manuel

---
