# 🍽️ Restaurant Management System

A complete, production-ready **Spring Boot 4** backend for the Restoran Management System — a secure REST API for managing restaurant reservations, tables, customers, and operations with JWT-based authentication.

---

## 📋 Project Overview
**Restoran Management System** is a Java/Spring Boot application backed by MySQL, designed to digitalize and streamline restaurant operations. The system provides:

* **Reservation Management:** Create, update, cancel and list reservations.
* **Table Tracking:** Monitor table availability and statuses in real-time.
* **Customer Records:** Store and manage customer information.
* **Secure Authentication:** JWT-based login and role-based access control (RBAC).
* **Input Validation:** Request validation with detailed error responses.
* **REST API:** Clean, structured endpoints for all operations.

---

## 🏗️ Technology Stack

| Category | Technology |
| :--- | :--- |
| **Framework** | Spring Boot 4.0.5 |
| **Language** | Java 17 |
| **Build** | Maven 3.9+ (Maven Wrapper included) |
| **Security** | Spring Security 6, JWT (Auth0 java-jwt 4.5.1) |
| **Database** | MySQL 8.0 |
| **ORM** | Spring Data JPA / Hibernate |
| **Validation** | Spring Boot Validation (Jakarta) |
| **Web** | Spring Web MVC |
| **Serialization** | Jackson + JSR310 (Java Time support) |
| **Testing** | JUnit 5, Spring Security Test, MockMvc |

---
```
restoran-management-system/
├── src/main/java/com/ironhack/restoran/
│   ├── 📁 config/
│   │   └── 📄 SecurityConfig.java                       
│   ├── 📁 controller/
│   │   ├── 📄 AuthController.java                       
│   │   ├── 📄 CategoryController.java                    
│   │   ├── 📄 OrderController.java                       
│   │   ├── 📄 ProductController.java                    
│   │   ├── 📄 RestaurantTableController.java             
│   │   └── 📄 UserController.java                       
│   ├── 📁 dto/
│   │   ├── 📁 request/                                   
│   │   └── 📁 response/                                  
│   ├── 📁 entity/
│   │   ├── 📄 BaseEntity.java                            
│   │   ├── 📄 Category.java                              
│   │   ├── 📄 Order.java                                 
│   │   ├── 📄 OrderItem.java                           
│   │   ├── 📄 Product.java                               
│   │   ├── 📄 Reservation.java                           
│   │   ├── 📄 RestaurantTable.java                       
│   │   └── 📄 User.java                                  
│   ├── 📁 enums/
│   │   ├── 📄 OrderStatus.java                           
│   │   ├── 📄 ReservationStatus.java                     
│   │   └── 📄 RoleName.java                             
│   ├── 📁 exception/
│   │   ├── 📄 BadRequestException.java
│   │   ├── 📄 ConflictException.java
│   │   ├── 📄 CustomAccessDeniedHandler.java
│   │   ├── 📄 CustomAuthenticationEntryPoint.java
│   │   ├── 📄 ForbiddenException.java
│   │   ├── 📄 GlobalExceptionHandler.java               
│   │   ├── 📄 ResourceNotFoundException.java
│   │   └── 📄 UnauthorizedException.java
│   ├── 📁 mapper/
│   │   ├── 📄 CategoryMapper.java
│   │   ├── 📄 OrderMapper.java
│   │   ├── 📄 ProductMapper.java
│   │   ├── 📄 ReservationMapper.java
│   │   ├── 📄 RestaurantTableMapper.java
│   │   └── 📄 UserMapper.java
│   ├── 📁 repository/
│   │   ├── 📄 CategoryRepository.java
│   │   ├── 📄 OrderItemRepository.java
│   │   ├── 📄 OrderRepository.java
│   │   ├── 📄 ProductRepository.java
│   │   ├── 📄 ReservationRepository.java
│   │   ├── 📄 RestaurantTableRepository.java
│   │   └── 📄 UserRepository.java
│   ├── 📁 security/
│   │   ├── 📄 CustomUserDetailsService.java             
│   │   ├── 📄 JwtAuthenticationFilter.java               
│   │   └── 📄 JwtTokenProvider.java                      
│   ├── 📁 service/
│   │   ├── 📄 AuthService.java
│   │   ├── 📄 CategoryService.java
│   │   ├── 📄 OrderService.java
│   │   ├── 📄 ProductService.java
│   │   ├── 📄 ReservationService.java
│   │   ├── 📄 RestaurantTableService.java
│   │   └── 📄 UserService.java
│   └── 📄 RestoranManagementSystemApplication.java       
│
├── 📁 src/main/resources/
│   └── 📄 application.properties                         
│
├── 📁 src/test/java/com/ironhack/restoran/
│   ├── 📄 AuthControllerTest.java
│   ├── 📄 AuthServiceTest.java
│   ├── 📄 CategoryServiceTest.java
│   ├── 📄 OrderServiceTest.java
│   ├── 📄 PasswordEncoderTest.java
│   ├── 📄 ProductServiceTest.java
│   ├── 📄 ReservationControllerTest.java
│   ├── 📄 ReservationServiceTest.java
│   ├── 📄 RestaurantTableControllerTest.java
│   ├── 📄 RestaurantTableServiceTest.java
│   ├── 📄 RestoranManagementSystemApplicationTests.java
│   ├── 📄 UserControllerTest.java
│   └── 📄 UserServiceTest.java
│
├── 📄 .gitattributes
├── 📄 .gitignore
├── 📄 mvnw
├── 📄 mvnw.cmd
├── 📄 pom.xml
└── 📄 README.md
```
# 🚀 Restaurant Management System API Documentation

### 🔐 1. Auth Controller
**Base Path:** `/api/auth`

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/register` | Register a new user and receive JWT | Public |
| `POST` | `/login` | Authenticate user and receive JWT | Public |

### 👤 2. User Controller
**Base Path:** `/api/users`

| Method | Endpoint | Description | Access |
| :--- | :--- | : :--- | :--- |
| `GET` | `/me` | Get current user profile details | Authenticated |
| `GET` | `/me/reservations` | Get current user's reservation list | Authenticated |
| `GET` | `/me/orders` | Get current user's order history | Authenticated |
| `GET` | `/` | List all registered users | Admin |
| `POST` | `/` | Create a new user manually | Admin |
| `PUT` | `/{id}` | Update existing user details | Admin |
| `DELETE` | `/{id}` | Remove a user from the system | Admin |

### 🍴 3. Category Controller
**Base Path:** `/api/categories`

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Get all menu categories | Public |
| `GET` | `/{id}` | Get a specific category by ID | Public |
| `POST` | `/` | Create a new menu category | Admin |
| `PUT` | `/{id}` | Update category name or description | Admin |
| `DELETE` | `/{id}` | Delete a category | Admin |

### 🍎 4. Product Controller
**Base Path:** `/api/products`

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Get all products | Public |
| `GET` | `/available` | Get only products currently in stock | Public |
| `GET` | `/category/{categoryId}` | Get products by category ID | Public |
| `GET` | `/{id}` | Get product details by ID | Public |
| `POST` | `/` | Add a new product to the menu | Admin |
| `PUT` | `/{id}` | Update product info or price | Admin |
| `DELETE` | `/{id}` | Delete a product from the menu | Admin |

### 🛍️ 5. Order Controller
**Base Path:** `/api/orders`

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/` | Create a new order for a table | Authenticated |
| `POST` | `/{orderId}/items` | Add new items to an existing order | Authenticated |
| `GET` | `/{id}` | Get specific order details | Owner/Admin |
| `GET` | `/` | List all orders in the system | Admin |
| `PATCH` | `/{id}/status` | Update order status (Preparing/Done) | Admin |

### 📅 6. Reservation Management
**Base Path:** `/api/reservations`

| Method | Endpoint | Description | Access   |
| :--- | :--- | :--- |:---------|
| `POST` | `/` | Create a new table reservation | Customer |
| `GET` | `/` | Get all reservations in the system | Public   |
| `GET` | `/{id}` | Get reservation details by ID | Public   |
| `GET` | `/user/{userId}` | Get all reservations for a specific user | Public   |
| `PATCH` | `/{id}/confirm` | Confirm a pending reservation | Admin    |
| `PATCH` | `/{id}/cancel` | Cancel an existing reservation | Admin    |
| `PUT` | `/{id}` | Update reservation details | Admin    |
| `DELETE` | `/{id}` | Permanently delete a reservation | Admin    |
| `GET` | `/status` | Filter reservations by status | Public   |
| `GET` | `/min_guests` | Find reservations by guest count | Public   |

### 🪑 7. Restaurant Table Controller
**Base Path:** `/api/restaurant`

| Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/` | Create a new restaurant table | Admin |
| `PUT` | `/{id}` | Update table capacity or number | Admin |
| `DELETE` | `/{id}` | Delete a table from the system | Admin |
| `GET` | `/status` | Find tables by availability status | Public |
| `GET` | `/capacity` | Find tables by minimum capacity | Public |
| `GET` | `/number/{tableNumber}` | Find a specific table by its number | Public |

🛡️ **Security Header:** All endpoints marked as Authenticated, Customer, or Admin require the JWT token:
`Authorization: Bearer <your_jwt_token>`

---

## 🚀 Quick Start

### Prerequisites
* ☕ Java JDK 17 or higher
* 🐬 MySQL Server 8.0+
* 🛠️ Maven 3.9+ (or use the included Maven Wrapper)

### 1. Clone the repository
```bash
git clone [https://github.com/BillerPlay/Restoran-Management-System.git](https://github.com/BillerPlay/Restoran-Management-System.git)
cd Restoran-Management-System
```
### 2. Setup the database
Log in to your MySQL server and run the following command:

```sql
CREATE DATABASE restoran_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
### ⚙️ Configuration
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/restoran_db
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_super_secret_key_min_32_characters
```
### 🚀 4. Build and Run
```Bash
Using Maven Wrapper
./mvnw clean install
./mvnw spring-boot:run
```
### ✅ Project Checklist
[x] REST API with Spring Boot 4.0.5

[x] JWT-based authentication (Auth0 java-jwt)

[x] Role-based access control with Spring Security 6 (ADMIN / USER)

[x] Spring Data JPA / Hibernate ORM

[x] MySQL database integration

[x] Jakarta Validation for input integrity

[x] 13 test classes covering Services, Controllers, and Security

⭐ If you like this project, don't forget to give it a star!
