package coms.w4156.moviewishlist.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coms.w4156.moviewishlist.models.User;
import coms.w4156.moviewishlist.services.UserService;

@RequestMapping(value = "/users")
@RestController
public class UserController {

    /**
     * Use dependency injection to inject an object of the UserService class.
     */
    @Autowired
    private UserService userService;

    /**
     * Fetch a kusr if all users in the database.
     * @return List of User objects
     */
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    /**
     * Get a particular use by ID. If not found HTTP 204: NO Content response.
     * @param id - The email address of the user
     * @return A single User object
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable final String id) {
        return userService.findById(id)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * POST `/users` will create a new user. The fields for the user object
     * must be passed in as the RequestBody as json.path.
     * @param user - User object to add to the database.
     * @return The user object that was just created
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody final User user) {
        if  (user.getEmail().isEmpty()
            || user.getName().isEmpty()
            || user.getPassword().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.create(user), HttpStatus.OK);
    }

    /**
     * PUT `/users/{id}` will update an existing user with the given ID.
     * The updated fields for the user should be passed in as the JSON
     * Request Body.
     *
     * @param id - email of the user to update
     * @param newData - user data for the updated user.
     * @return The newly updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
        @PathVariable final String id,
        @RequestBody final User newData
    ) {
        return userService.findById(id)
            .map(user -> {
                user.setName(newData.getName());
                userService.update(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * Delete all users.
     * @return The list of users that were just deleted
     */
    @DeleteMapping
    public ResponseEntity<List<User>> deleteAllUsers() {
        userService.deleteAll();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }


    /**
     * Delete a particular user by ID.
     * @param id The email of the user to delete
     * @return the user that was just deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteWishlist(@PathVariable final String id) {
        return userService.deleteById(id)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

}
