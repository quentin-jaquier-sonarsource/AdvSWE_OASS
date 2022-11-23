# Users (`/users`)

These are the endpoints that can be used to complete CRUD operations for Users.

## Endpoints

### **POST** `/new-client?{email}`

This creates an entry in the `Client` table. The email has to be unique. The reponse contains the client's JWT.
