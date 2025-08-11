# 🛡️ ToDo App with Auth Microservice

This is a **microservice-based ToDo application** built with **Spring Boot**. It includes:

- A central **API Gateway**
- An **Auth Service** for login/register and JWT generation
- A **ToDo Service** that is protected and requires authentication

---

## 🧱 Technologies Used

- Spring Boot 3.x
- Spring Security 6.x
- OAuth2 Resource Server
- RSA + JWT (RS256)
- API Gateway (Spring Cloud Gateway)
- PostgreSQL

---

## 📦 Services

| Service           | Port | Description                     |
|-------------------|------|---------------------------------|
| API Gateway       | 8080 | Routes all incoming requests    |
| Auth Microservice | 8083 | Handles login/register & tokens |
| ToDo Microservice | 8082 | CRUD for to-do items (secured)  |

---

## ▶️ Quick Start

1. Generate RSA keys:
   ```bash
   openssl genrsa -out private_key.pem 2048
   openssl rsa -in private_key.pem -pubout -out public_key.pem
