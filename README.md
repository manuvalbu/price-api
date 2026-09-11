
# 🧾 Price API

Spring Boot REST API that returns the **applicable price** for a product given a brand and an application date/time.

When several prices match, the one with the **highest priority** is selected.

---

## Features

- **Hexagonal architecture (Ports & Adapters)**
    - **Domain:** pure business rules (`Price`, `DateRange`, `Currency`, `PriceResolver`)
    - **Application:** use case, ports, DTOs, and domain bean wiring
    - **Infrastructure:** REST, JPA, Flyway, exception handling
- **Database-side filtering** with Spring Data JPA Specifications (product, brand, date range, order by priority)
- **Domain validation** (record invariants and value objects)
- **HTTP input validation** on request parameters
- **Flyway migrations** with official sample data
- **OpenAPI / Swagger** documentation
- **Unit, integration, and end-to-end tests**
- **H2 in-memory database** for local development and tests

---

## 🚀 Tech Stack

| Area | Technology |
|------|------------|
| Language | Java 17+ |
| Framework | Spring Boot 4.x |
| Persistence | Spring Data JPA + Specifications |
| Database (local/test) | H2 in-memory |
| Migrations | Flyway (`spring-boot-starter-flyway`) |
| API docs | springdoc-openapi |
| Tests | JUnit 5, Mockito, AssertJ, Spring Boot Test |

---

## 📌 Business Logic

1. A price is applicable when `startDate ≤ requestedDate ≤ endDate`.
2. If several prices apply, the one with the **highest priority** wins.
3. If none apply → **HTTP 404**.

---

## 🧠 Architecture

This project follows **Hexagonal Architecture** (Ports & Adapters).

```
┌─────────────────────────────────────────────────────────────┐
│                     Infrastructure                          │
│  ┌──────────────────┐              ┌─────────────────────┐  │
│  │  REST Adapter    │              │  Persistence        │  │
│  │  (Controller)    │              │  Adapter + JPA      │  │
│  │  Exception       │              │  Specifications     │  │
│  │  Handler         │              │  Flyway / H2        │  │
│  └────────┬─────────┘              └──────────▲──────────┘  │
│           │                                   │             │
│           │ invokes                           │ implements  │
└───────────┼───────────────────────────────────┼─────────────┘
            │                                   │
┌───────────┼───────────────────────────────────┼─────────────┐
│           │         Application               │             │
│  ┌────────▼─────────┐              ┌──────────┴──────────┐  │
│  │ FindPriceUseCase │─────────────►│  PriceRepository    │  │
│  │ FindPriceService │   uses port  │  (outbound port)    │  │
│  │ DTOs / Mappers   │              └─────────────────────┘  │
│  └────────┬─────────┘                                       │
└───────────┼─────────────────────────────────────────────────┘
            │ uses
            ▼
┌──────────────────────┐
│        Domain        │
│  Price, DateRange    │
│  Currency            │
│  PriceResolver       │
│  Domain exceptions   │
└──────────────────────┘
```

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
  "price": 25.45
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
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DDB_USERNAME=inditex -DDB_PASSWORD=inditex"
```

Or from your IDE configuration with environment variables DB_USERNAME=inditex and DB_PASSWORD=inditex

---

## 🧪 Running Tests

```bash
mvn test
```
---


## 👤 Author

* Manuel

---
