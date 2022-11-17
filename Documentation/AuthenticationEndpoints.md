# Users (`/users`)

These are the endpoints that can be used to complete CRUD operations for Users.

## Endpoints

### **POST** `/signup?{username}&{email}&{password}`

This creates an entry in the `User` table. The username and the email have to be unique. The response contains the user's JWT.

### **POST** `/login` (`username, password`)

If the credentials are correct, the response contains the user's JWT.
