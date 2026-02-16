
# GEDS – Group Expense Distribution System (Backend)

This project is a **Spring Boot REST API** for managing **users, groups, and shared expenses**.
It follows a **layered architecture** using **Controllers → Services → Repositories → Entities**, with **DTOs** and **global exception handling**.

---

## 🧱 Project Architecture

```
Controller  →  Service  →  Repository  →  Entity
                ↑
               DTO
```

- **Controller**: Exposes REST APIs
- **Service**: Business logic
- **Repository**: Database access (JPA)
- **Entity**: Database tables
- **DTO**: Request/Response objects
- **Config**: Global exception handling

---

## 🧩 Entities (Database Models)

### 👤 User (`entity/User.java`)
Represents an application user.

**Main fields:**
- `id` – unique user ID
- `email` – unique email
- `password` – encrypted password

**Used in:**
- Authentication
- Group ownership
- Expense payment tracking

---

### 👥 Group (`entity/Group.java`)
Represents a group where expenses are shared.

**Main fields:**
- `id`
- `name`
- `owner` (User)

---

### 👥 GroupMember (`entity/GroupMember.java`)
Links users to groups.

**Main fields:**
- `id`
- `user`
- `group`

---

### 💰 Expense (`entity/Expense.java`)
Represents a group expense.

**Main fields:**
- `id`
- `title`
- `amount`
- `group`
- `paidBy` (User)

---

### 💸 ExpenseSplit (`entity/ExpenseSplit.java`)
Stores how an expense is split among users.

---

## 🧠 Services Layer

Services contain **business logic** and are called by controllers.

### `UserService`
- Create user
- Login user
- Validate email & password

### `GroupService`
- Create group
- Rename group
- Delete group

### `ExpenseService`
- Add expense
- Update expense
- Manage expense splits

✅ Controllers **never talk directly to repositories**.

---

## 🌐 Controllers & API Endpoints

---

## 👤 UserController

**Base URL:** `/auth`

---

### ✅ Register User
**POST** `/auth/register`

**Request Payload (`UserRequest`):**
```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

**Success Response (`UserResponse`) – 201 CREATED:**
```json
{
  "id": 1,
  "email": "user@example.com"
}
```

**Possible Errors:**
- Email already exists → `409 CONFLICT`

---

### ✅ Login User
**POST** `/auth/login`

**Request Payload:**
```json
{
  "email": "user@example.com",
  "password": "123456"
}
```

**Success Response – 200 OK:**
```json
{
  "id": 1,
  "email": "user@example.com"
}
```

**Possible Errors:**
- User not found
- Invalid password

---

## 👥 GroupController

**Base URL:** `/groups`

---

### ✅ Create Group
**POST** `/groups`

**Request Payload (`GroupRequest`):**
```json
{
  "name": "Trip to Dubai",
  "ownerId": 1
}
```

**Response (`GroupResponse`):**
```json
{
  "id": 10,
  "name": "Trip to Dubai",
  "ownerId": 1
}
```

---

### ✅ Update Group Name
**PUT** `/groups`

**Request Payload (`GroupNameChangeReq`):**
```json
{
  "groupId": 10,
  "newName": "Dubai Vacation"
}
```

**Response:**
```text
Group name updated successfully
```

---

### ✅ Delete Group
**DELETE** `/groups/{name}`

**Example:**
```
DELETE /groups/Dubai Vacation
```

**Response:**
```text
Group deleted successfully
```

---

## 💰 ExpenseController

**Base URL:** `/expense`

---

### ✅ Add Expense
**POST** `/expense`

**Request Payload (`ExpenseReq`):**
```json
{
  "groupId": 10,
  "title": "Hotel",
  "amount": 500,
  "paidByUserId": 1
}
```

**Response (`ExpenseRes`):**
```json
{
  "id": 101,
  "title": "Hotel",
  "amount": 500,
  "groupId": 10,
  "paidByUserId": 1
}
```

---

### ✅ Update Expense
**POST** `/expense/{id}`

**Example:**
```
POST /expense/101
```

**Request Payload:**
```json
{
  "title": "Luxury Hotel",
  "amount": 750
}
```

**Response:**
```json
{
  "id": 101,
  "title": "Luxury Hotel",
  "amount": 750,
  "groupId": 10,
  "paidByUserId": 1
}
```

---

## ⚙️ Configuration Layer

### 🌍 GlobalExceptionHandler

File:
```
config/GlobalExceptionHandler.java
```

Handles **all application errors centrally**.

---

### ✅ IllegalArgumentException
**HTTP 400**
```json
{
  "status": 400,
  "message": "Invalid request data"
}
```

---

### ✅ EmailAlreadyExistsException
**HTTP 409**
```json
{
  "status": 409,
  "message": "Email already exists"
}
```

---

### ✅ UserNotFoundException
**HTTP 409**
```json
{
  "status": 409,
  "message": "User not found"
}
```

---

### ✅ General Exception
**HTTP 500**
```json
{
  "status": 500,
  "message": "Something went wrong"
}
```

---

## ✅ Summary

✔ Clear layered architecture  
✔ DTO‑based clean APIs  
✔ Real payloads & responses  
✔ Centralized error handling  
✔ Ready for frontend (React / Mobile)

---

If you want next:
- ✅ **Swagger/OpenAPI**
- ✅ **JWT Authentication**
- ✅ **Database schema diagram**
- ✅ **Postman collection**

Just tell me.
