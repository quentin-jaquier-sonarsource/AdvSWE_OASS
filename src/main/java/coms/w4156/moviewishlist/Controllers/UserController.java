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

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.findById(id)
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.create(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User newData) {
        return userService.findById(id)
            .map(user -> {
                user.setName(newData.getName());
                userService.update(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @DeleteMapping
    public ResponseEntity<List<User>> deleteAllUsers() {
        userService.deleteAll();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteWishlist(@PathVariable String id) {
        return userService.deleteById(id)
            .map(deletedUser -> new ResponseEntity<>(deletedUser, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));   
    }

}
