## Authentication Endpoints

### **POST** `/new-client?{email}`

This creates an entry in the `Client` table. The email has to be unique. The reponse contains the client's JWT.
