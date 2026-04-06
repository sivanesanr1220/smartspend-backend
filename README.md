# SmartSpend Backend - Spring Boot 3

## Tech Stack
- Spring Boot 3.2, Spring Security, Spring Data JPA
- MySQL 8.0, JWT, BCrypt, JavaMailSender
- WebSocket (STOMP), Swagger/OpenAPI

## Setup

### 1. Create MySQL Database
```sql
CREATE DATABASE smartspend_db;
```

### 2. Configure application.properties
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### 3. Run
```bash
mvn spring-boot:run
```

API: http://localhost:8080
Swagger: http://localhost:8080/swagger-ui.html

## API Endpoints

### Auth (Public)
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/auth/register | Register + send verification email |
| POST | /api/auth/login | Login → JWT token |
| GET | /api/auth/verify?token= | Verify email |
| POST | /api/auth/forgot-password?email= | Send reset link |
| POST | /api/auth/reset-password | Reset with token |

### Expenses (JWT Required)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/expenses | Get all (filterable by type, category, date) |
| POST | /api/expenses | Create transaction |
| PUT | /api/expenses/{id} | Update |
| DELETE | /api/expenses/{id} | Delete |

### Budgets (JWT Required)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/budgets?month=&year= | Get budgets for month |
| POST | /api/budgets | Create/update budget |
| DELETE | /api/budgets/{id} | Delete budget |

### Dashboard (JWT Required)
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/dashboard | Stats, recent txns, budgets |
| GET | /api/dashboard/notifications | User notifications |
| POST | /api/dashboard/notifications/read | Mark all read |
