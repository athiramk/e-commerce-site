#Detailed Design Document


## Phase 0 — Requirements & Planning

---

### Step 1 — Understand the Domain
**Output: Domain Understanding Document** (informal)

```
Document: domain-understanding.md

## Business Context
A multi-vendor e-commerce platform where vendors sell products
and customers place orders online.

## Actors / Users
- Customer  → browses, orders, pays, tracks
- Vendor    → manages products, views sales
- Admin     → manages users, views reports, resolves disputes

## Core Business Concepts
- A Customer can place many Orders
- An Order has multiple Products from potentially different Vendors
- Inventory is tracked per Product
- Payment is collected per Order
- Vendor earns revenue per delivered Order

## Business Rules (Critical)
- A customer cannot order more than available stock
- An order cannot be cancelled after it is shipped
- A vendor can only edit their own products
- Payment must succeed before order is confirmed
- Admin can suspend any user account

## Out of Scope (for now)
- Shipping/logistics integration
- Reviews and ratings
- Discount / coupon system
- Email notifications
```

---

### Step 2 — Write User Stories
**Output: User Story Document / Jira Epics & Stories**

```
Document: user-stories.md

━━━━━━━━━━━━━━━━━━━━━━━━━━
EPIC 1: User Management
━━━━━━━━━━━━━━━━━━━━━━━━━━

US-01: As a visitor,  I can register as a Customer with name, email, password
US-02: As a visitor,  I can register as a Vendor with business name, email, password
US-03: As a user,     I can log in and receive an access token
US-04: As a customer, I can add/edit/delete my delivery addresses
US-05: As an admin,   I can suspend or reactivate a user account

━━━━━━━━━━━━━━━━━━━━━━━━━━
EPIC 2: Product Catalog
━━━━━━━━━━━━━━━━━━━━━━━━━━

US-06: As a vendor,   I can create a product with name, description, price, category
US-07: As a vendor,   I can update my product details and stock quantity
US-08: As a vendor,   I can deactivate a product (hide from catalog)
US-09: As a customer, I can browse products filtered by category, price range, keyword
US-10: As a customer, I can view a product detail page

━━━━━━━━━━━━━━━━━━━━━━━━━━
EPIC 3: Order Management
━━━━━━━━━━━━━━━━━━━━━━━━━━

US-11: As a customer, I can place an order with one or more products
US-12: As a customer, I can view my order history (paginated)
US-13: As a customer, I can view details of a specific order
US-14: As a customer, I can cancel a PENDING order
US-15: As a vendor,   I can update order status (CONFIRMED → SHIPPED → DELIVERED)

━━━━━━━━━━━━━━━━━━━━━━━━━━
EPIC 4: Payment
━━━━━━━━━━━━━━━━━━━━━━━━━━

US-16: As a customer, I can pay for an order via card or UPI
US-17: As a customer, I can see payment status on my order

━━━━━━━━━━━━━━━━━━━━━━━━━━
EPIC 5: Admin & Reports
━━━━━━━━━━━━━━━━━━━━━━━━━━

US-18: As an admin, I can view all users and filter by role/status
US-19: As an admin, I can view vendor-wise sales report
US-20: As an admin, I can view monthly revenue report


━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Acceptance Criteria Example (for US-11)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GIVEN I am a logged-in customer
WHEN  I place an order with valid products and sufficient stock
THEN  order is created with status PENDING
AND   inventory is reduced for each product
AND   I receive the order details in response

GIVEN I place an order where one product has insufficient stock
THEN  the entire order is rejected
AND   no inventory is reduced
AND   I receive a clear error message
```

> In Jira, each `US-XX` becomes a **Story** under an **Epic**. Developers pick stories from the backlog in each sprint.

---

### Step 3 — Identify Bounded Contexts
**Output: Module Breakdown Document + Package Structure**

```
Document: architecture-modules.md

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODULE MAP
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

┌──────────────────────────────────────────────────────┐
│                    E-Commerce Platform                │
├───────────┬───────────┬───────────┬───────────────────┤
│   User    │  Product  │   Order   │     Payment       │
│  Module   │  Module   │  Module   │     Module        │
├───────────┼───────────┼───────────┼───────────────────┤
│ User      │ Product   │ Order     │ Payment           │
│ Role      │ Category  │ OrderItem │ CardPayment       │
│ Address   │ Inventory │           │ UpiPayment        │
├───────────┼───────────┼───────────┼───────────────────┤
│ register  │ create    │ place     │ process           │
│ login     │ search    │ cancel    │ refund            │
│ suspend   │ update    │ track     │                   │
└───────────┴───────────┴───────────┴───────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
MODULE RESPONSIBILITIES
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

User Module
  - Owns: User, Role, Address
  - Responsible for: registration, auth, role management
  - Does NOT handle: orders, payments

Product Module
  - Owns: Product, Category, Inventory
  - Responsible for: catalog, search, stock tracking
  - Does NOT handle: who ordered what

Order Module
  - Owns: Order, OrderItem
  - Responsible for: placing orders, order lifecycle
  - Depends on: User (customer), Product (items)
  - Triggers: Payment processing

Payment Module
  - Owns: Payment, CardPayment, UpiPayment
  - Responsible for: payment processing, status tracking
  - Depends on: Order

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PACKAGE STRUCTURE
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

com.ecommerce
├── user/
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── dto/
├── product/
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── dto/
├── order/
│   └── ...
├── payment/
│   └── ...
└── common/
    ├── exception/
    ├── response/
    └── config/
```

---

## Phase 1 — System Design

---

### Step 4 — Design the Data Model
**Output: ERD (Entity Relationship Diagram) + Data Dictionary**

```
Document: data-model.md

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
ERD (Text representation)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[users] ──< [addresses]         (one user → many addresses)
[users] >─< [roles]             (many-to-many via user_roles)
[users] ──< [orders]            (one user → many orders)
[users] ──< [products]          (one vendor → many products)
[orders] ──< [order_items]      (one order → many items)
[order_items] >── [products]    (many items → one product)
[orders] ──  [payments]         (one order → one payment)
[products] ── [inventory]       (one product → one inventory)
[products] >── [categories]     (many products → one category)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
DATA DICTIONARY
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

TABLE: users
┌─────────────┬──────────────┬──────────┬─────────────────────────┐
│ Column      │ Type         │ Nullable │ Notes                   │
├─────────────┼──────────────┼──────────┼─────────────────────────┤
│ id          │ BIGINT       │ NO       │ PK, auto increment      │
│ email       │ VARCHAR(100) │ NO       │ Unique                  │
│ password    │ VARCHAR(255) │ NO       │ bcrypt hashed           │
│ full_name   │ VARCHAR(100) │ NO       │                         │
│ status      │ VARCHAR(20)  │ NO       │ ACTIVE/SUSPENDED        │
│ version     │ INT          │ NO       │ Optimistic lock         │
│ created_at  │ TIMESTAMP    │ NO       │ Auto-set on insert      │
│ updated_at  │ TIMESTAMP    │ NO       │ Auto-set on update      │
└─────────────┴──────────────┴──────────┴─────────────────────────┘

TABLE: products
┌─────────────┬──────────────┬──────────┬─────────────────────────┐
│ Column      │ Type         │ Nullable │ Notes                   │
├─────────────┼──────────────┼──────────┼─────────────────────────┤
│ id          │ BIGINT       │ NO       │ PK                      │
│ name        │ VARCHAR(200) │ NO       │ Index for search        │
│ description │ TEXT         │ YES      │                         │
│ price       │ DECIMAL(10,2)│ NO       │                         │
│ status      │ VARCHAR(20)  │ NO       │ ACTIVE/DISCONTINUED     │
│ vendor_id   │ BIGINT       │ NO       │ FK → users.id           │
│ category_id │ BIGINT       │ NO       │ FK → categories.id      │
│ version     │ INT          │ NO       │ Optimistic lock         │
└─────────────┴──────────────┴──────────┴─────────────────────────┘

TABLE: orders
┌──────────────┬──────────────┬──────────┬─────────────────────────┐
│ Column       │ Type         │ Nullable │ Notes                   │
├──────────────┼──────────────┼──────────┼─────────────────────────┤
│ id           │ BIGINT       │ NO       │ PK                      │
│ order_number │ VARCHAR(50)  │ NO       │ Unique, e.g ORD-001     │
│ customer_id  │ BIGINT       │ NO       │ FK → users.id           │
│ status       │ VARCHAR(20)  │ NO       │ PENDING/CONFIRMED/...   │
│ total_amount │ DECIMAL(10,2)│ NO       │                         │
│ address_id   │ BIGINT       │ NO       │ FK → addresses.id       │
│ version      │ INT          │ NO       │ Optimistic lock         │
│ ordered_at   │ TIMESTAMP    │ NO       │                         │
└──────────────┴──────────────┴──────────┴─────────────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
INDEXES PLANNED
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
users        → idx_users_email (email)
products     → idx_products_vendor (vendor_id)
              idx_products_category (category_id)
              idx_products_status (status)
orders       → idx_orders_customer (customer_id)
              idx_orders_status (status)
order_items  → idx_order_items_order (order_id)
              idx_order_items_product (product_id)
```

> In enterprises, this is drawn visually in tools like **draw.io, Lucidchart, or dbdiagram.io** and stored in Confluence.

---

### Step 5 — Design the API Contract
**Output: OpenAPI Specification (openapi.yaml or Swagger UI)**

```yaml
# openapi.yaml

openapi: 3.0.3
info:
  title: E-Commerce API
  version: 1.0.0

paths:

  # ── Auth ──────────────────────────────
  /api/v1/auth/register:
    post:
      summary: Register a new user
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RegisterRequest'
      responses:
        '201':
          description: User registered
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/UserResponse'
        '409':
          description: Email already exists

  /api/v1/auth/login:
    post:
      summary: Login and get JWT token
      responses:
        '200':
          description: Login successful
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/LoginResponse'
        '401':
          description: Invalid credentials

  # ── Products ──────────────────────────
  /api/v1/products:
    get:
      summary: Search products (public)
      parameters:
        - name: category
          in: query
          schema:
            type: integer
        - name: minPrice
          in: query
          schema:
            type: number
        - name: keyword
          in: query
          schema:
            type: string
        - name: page
          in: query
          schema:
            type: integer
            default: 0
        - name: size
          in: query
          schema:
            type: integer
            default: 20
      responses:
        '200':
          description: Paginated product list

    post:
      summary: Create a product (Vendor only)
      security:
        - bearerAuth: []
      responses:
        '201':
          description: Product created
        '403':
          description: Not a vendor

  # ── Orders ────────────────────────────
  /api/v1/orders:
    post:
      summary: Place an order (Customer only)
      security:
        - bearerAuth: []
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/PlaceOrderRequest'
      responses:
        '201':
          description: Order placed
        '400':
          description: Insufficient stock
        '404':
          description: Product not found

  /api/v1/orders/my-orders:
    get:
      summary: Get my order history (Customer)
      security:
        - bearerAuth: []
      parameters:
        - name: page
          in: query
          schema:
            type: integer
      responses:
        '200':
          description: Paginated order list

components:
  schemas:
    RegisterRequest:
      type: object
      required: [email, password, fullName, role]
      properties:
        email:
          type: string
        password:
          type: string
          minLength: 8
        fullName:
          type: string
        role:
          type: string
          enum: [CUSTOMER, VENDOR]

    PlaceOrderRequest:
      type: object
      required: [items, shippingAddressId]
      properties:
        items:
          type: array
          items:
            $ref: '#/components/schemas/OrderItemRequest'
        shippingAddressId:
          type: integer

    OrderItemRequest:
      type: object
      required: [productId, quantity]
      properties:
        productId:
          type: integer
        quantity:
          type: integer
          minimum: 1

  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT
```

> This file is the **contract between backend and frontend**. Frontend devs use tools like **Postman** or **Swagger UI** to mock responses from this spec while backend is being built.

---

### Step 6 — Define Request/Response DTOs
**Output: DTO Design Document**

```
Document: dto-design.md

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
USER MODULE DTOs
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

RegisterRequest
  - email: String (required, valid email format)
  - password: String (required, min 8 chars)
  - fullName: String (required)
  - role: Enum CUSTOMER | VENDOR (required)

LoginRequest
  - email: String (required)
  - password: String (required)

LoginResponse
  - accessToken: String
  - tokenType: String  ("Bearer")
  - expiresIn: Long    (seconds)

UserResponse
  - id: Long
  - email: String
  - fullName: String
  - status: String
  - roles: List<String>
  - createdAt: LocalDateTime
  ❌ NO password field — never expose this

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
PRODUCT MODULE DTOs
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ProductRequest (create/update)
  - name: String (required)
  - description: String
  - price: BigDecimal (required, positive)
  - categoryId: Long (required)
  - stockQuantity: Integer (required, min 0)

ProductResponse
  - id: Long
  - name: String
  - description: String
  - price: BigDecimal
  - category: CategoryResponse
  - stockQuantity: Integer
  - status: String
  - vendorName: String
  ❌ NO vendor password, NO internal fields

ProductSummary  (for list/search results - lighter payload)
  - id: Long
  - name: String
  - price: BigDecimal
  - category: String
  - stockQuantity: Integer

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
ORDER MODULE DTOs
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

PlaceOrderRequest
  - items: List<OrderItemRequest>
      - productId: Long
      - quantity: Integer (min 1)
  - shippingAddressId: Long

OrderResponse
  - id: Long
  - orderNumber: String
  - status: String
  - items: List<OrderItemResponse>
      - productId: Long
      - productName: String
      - quantity: Integer
      - unitPrice: BigDecimal
      - subtotal: BigDecimal
  - totalAmount: BigDecimal
  - shippingAddress: AddressResponse
  - payment: PaymentResponse
  - orderedAt: LocalDateTime

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
STANDARD API WRAPPER (all responses)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ApiResponse<T>
  - success: boolean
  - message: String
  - data: T
  - timestamp: LocalDateTime

Success example:
{
  "success": true,
  "message": "Order placed successfully",
  "data": { ...OrderResponse... },
  "timestamp": "2024-05-01T10:30:00"
}

Error example:
{
  "success": false,
  "message": "Insufficient stock for product: iPhone 15",
  "data": null,
  "timestamp": "2024-05-01T10:30:00"
}
```

---

## Phase 2 — Project Setup
---
### Step 7 — Project Structure First
```
src/main/java/com/ecommerce/
│
├── common/                        # Shared across modules
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── BusinessException.java
│   ├── response/
│   │   └── ApiResponse.java       # Standard response wrapper
│   └── audit/
│       └── AuditConfig.java
│
├── user/                          # User module (self-contained)
│   ├── entity/
│   │   ├── User.java
│   │   ├── Role.java
│   │   └── Address.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── service/
│   │   └── UserService.java
│   ├── controller/
│   │   └── UserController.java
│   └── dto/
│       ├── RegisterRequest.java
│       └── UserResponse.java
│
├── product/                       # Product module
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── dto/
│
├── order/                         # Order module
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── dto/
│
└── payment/                       # Payment module
    ├── entity/
    ├── repository/
    ├── service/
    ├── controller/
    └── dto/
```

---

### Step 8 — Setup Infrastructure
```
- Spring Boot project (Spring Initializr)
- Add dependencies (JPA, Flyway, Security, Validation, Lombok)
- Configure application.properties (DB, HikariCP, JPA settings)
- Create first Flyway migration file
- Setup GlobalExceptionHandler
- Setup ApiResponse wrapper
```

---

## Phase 3 — Implementation Order (Per Feature)

### The Right Order Within Each Feature

```
For EVERY feature, always follow this order:

1. Database migration (Flyway SQL)
      ↓
2. Entity class
      ↓
3. Repository interface
      ↓
4. DTOs (Request + Response)
      ↓
5. Service (business logic)
      ↓
6. Controller (expose API)
      ↓
7. Test it (Postman / unit test)
```

**Why this order?**
- DB migration = source of truth
- Entity = mirrors the DB
- Repository = data access
- DTO = what the API exposes (never expose raw entities)
- Service = business logic lives here
- Controller = thin layer, just delegates to service

---

## Phase 4 — Feature-by-Feature Breakdown for Your Project

Here's exactly how to split your e-commerce project into tasks:

---

### 🔧 Task 1 — Project Bootstrap
```
□ Create Spring Boot project
□ Add dependencies: JPA, MySQL, Flyway, Lombok, Validation, Spring Security
□ Configure application.properties
□ Create ApiResponse<T> wrapper class
□ Create GlobalExceptionHandler
□ Verify app starts without errors
```

---

### 🔧 Task 2 — User Module
```
□ Write V1__create_users_roles_addresses.sql (Flyway)
□ Create User, Role, Address entities
□ Create UserRepository, RoleRepository
□ Create RegisterRequest, LoginRequest, UserResponse DTOs
□ Create UserService (register, findByEmail)
□ Create UserController (POST /auth/register)
□ Test with Postman
```

---

### 🔧 Task 3 — Authentication (Spring Security + JWT)
```
□ Add JWT dependency
□ Create JwtService (generate, validate token)
□ Create SecurityFilterChain config
□ Implement login endpoint → returns JWT token
□ Protect routes by role (CUSTOMER, VENDOR, ADMIN)
□ Test login + protected routes in Postman
```

---

### 🔧 Task 4 — Product Module
```
□ Write V2__create_categories_products_inventory.sql
□ Create Category, Product, Inventory entities
□ Create ProductRepository with Specifications
□ Create ProductRequest, ProductResponse DTOs
□ Create ProductService (create, update, search with filters)
□ Create ProductController
    GET  /products (paginated + filtered)
    GET  /products/{id}
    POST /products (Vendor only)
    PUT  /products/{id} (Vendor only)
□ Test all endpoints
```

---

### 🔧 Task 5 — Order Module
```
□ Write V3__create_orders_order_items.sql
□ Create Order, OrderItem entities
□ Create OrderRepository (with JOIN FETCH to avoid N+1)
□ Create PlaceOrderRequest, OrderResponse DTOs
□ Create OrderService
    - Validate stock (pessimistic lock on inventory)
    - Deduct inventory
    - Create order + items (cascade)
    - Calculate total
□ Create OrderController
    POST /orders
    GET  /orders/{id}
    GET  /orders/my-orders (paginated)
    PUT  /orders/{id}/cancel
□ Test happy path + insufficient stock scenario
```

---

### 🔧 Task 6 — Payment Module
```
□ Write V4__create_payments.sql
□ Create Payment (abstract), CardPayment, UpiPayment entities (Inheritance)
□ Create PaymentRepository
□ Create PaymentService (process payment, handle failure)
□ Create PaymentController
    POST /payments/{orderId}
□ Test payment success + failure
```

---

### 🔧 Task 7 — Admin & Reports
```
□ Create AdminController (protected by ADMIN role)
□ Add JPQL aggregation queries to OrderRepository
    - Vendor sales report
    - Monthly revenue
□ Add bulk operations (bulk suspend users, bulk update product status)
□ Test all admin endpoints
```

---

### 🔧 Task 8 — Production Hardening
```
□ Add 2nd level cache on Product entity (EhCache)
□ Tune HikariCP settings
□ Enable slow query logging
□ Add @Version (optimistic locking) everywhere
□ Review all relationships — ensure LAZY loading
□ Add missing DB indexes
□ Write integration tests for critical flows
```

---

## How a Real Enterprise Team Tracks This

In companies, each task above becomes a **Jira ticket**:

```
Epic: User Management
  Story: As a customer, I can register
    Task: Create Flyway migration for users table
    Task: Create User entity
    Task: Create UserRepository
    Task: Create RegisterRequest DTO + validation
    Task: Create UserService.register()
    Task: Create POST /auth/register endpoint
    Task: Write unit test for UserService
    Task: Write integration test for register API
```


---

## Golden Rules Enterprise Developers Follow

```
1. Never expose entities directly in API responses → always use DTOs
2. Controllers are THIN → they only call services, never contain logic
3. Services own the business logic → not controllers, not repositories
4. One transaction per service method → not per repository call
5. Repositories only do data access → no business logic
6. Fail fast → validate inputs at controller layer (@Valid)
7. Never trust the client → always validate on server side
8. Write migrations first → never let Hibernate auto-create schema in production
9. Every PR is one feature → small, reviewable, testable
10. Test the service layer → not just the happy path
```

---

## Summary — Your Action Plan

```
Week 1  → Phase 0 + 1: Requirements, ERD, API design on paper
Week 2  → Phase 2: Project setup, structure, infrastructure
Week 3  → Task 2 + 3: User module + Auth
Week 4  → Task 4: Product module + Specifications
Week 5  → Task 5: Order module + Locking + N+1 fix
Week 6  → Task 6: Payment + Inheritance mapping
Week 7  → Task 7: Admin reports + Bulk ops
Week 8  → Task 8: Production hardening + Tests
```
