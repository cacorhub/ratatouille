# API Endpoints - Users

## `POST /api/users`

This endpoint should be used to create a user registration.

When this endpoint is called a `User` entity is persisted on database with status as pending activation.

**Expected JSON body:**

```json
{
  "user": {
    "name": "Manuel Gomes",
    "cpf": "23267661560",
    "telegram-chat-id": "123456789"
  }
}
```

**The successful response for a user creation request is the following:**

`STATUS 200 - OK`

```json
{
  "user": {
    "id": "566b898a-86d2-4edd-89c9-793b22a5057c",
    "name": "Manuel Gomes",
    "cpf": "23267661560",
    "telegram-chat-id": "123456789",
    "status": "pending-activation"
  }
}
```