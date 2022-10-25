# Users (`/users`)

These are the endpoints that can be used to complete CRUD operations for Users.

## `User` Model

A `User` object has the following fields:

- **String** _email_: The email address of the user. Must be unique. The email is also used as the "primary key" of a user, and cannot be changed once set.

- **String** _name_: The name of the user. This is the full name or display name of a user. It can be changed any time.

- **String** _password_: The password of a user. This field is currently stored as plain-text, but will be stored as a secure Hash in the future.

- **`List<Wishlist>`** wishlists: A list of [Wishlist](/Documentation/WishlistEndpoints.md#wishlist-model) objects that are _owned_ by this user.

## Endpoints

### **GET** `/users` (`List<User>`)

This will fetch a list of all **Users** in the database.

### **GET** `/users/{email}` (`User`)

By passing the `email` as a path variable, will return a single User. If the user doesn't exist you will get Status 204: No Content instead.

### **POST** `/users` (`User`)

Making a `POST` requst to the `/users` will result in the creation of a new User Object in the database which will be returned as the response.

In order to create a user, you also need to pass a JSON request body containing the following fields:

```ts
type RequestBody = {
  email: String;
  name: String;
  password: String;
};
```

### **PUT** `/users/{email}` (`User`)

By making a `PUT` request you can update an existing user. If a user with the given email doesn't exist you'll get a Status 204: No Content error instead.

Similar to when creating a user, you must also pass a JSON request body with the fields to update the existing user. However, in this case the fields are optional:

```ts
type RequestBody = {
  name?: String;
  password?: String;
};
```

**NOTE:** Once the user has been created, you cannot later change the email of the object.

### **DELETE** `/users` (`List<User>`)

This will **delete** of all **Users** in the database. The delated users will be returned in a JSON response.

### **DELETE** `/users/{email}` (`User`)

This will **delete** of the user with the given `email`. The deleted user object is returned in the response.

If the given `email` does not match any existing user, a Status 204: No Content error response will be returned instead.
