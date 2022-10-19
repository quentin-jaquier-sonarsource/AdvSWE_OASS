package coms.w4156.moviewishlist.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.UserService;
import coms.w4156.moviewishlist.services.WishlistService;

@RequestMapping(value = "/wishlists")
@RestController
public class WishlistController {

    @Autowired
    WishlistService wlService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<Wishlist>> getAllWishlists() {
        List<Wishlist> wlList = wlService.getAll();
        return new ResponseEntity<>(wlList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable long id) {
        return wlService.findById(id)
            .map(wishlist -> new ResponseEntity<>(wishlist, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
    
    @PostMapping
    public ResponseEntity<Wishlist> createWishlist(@RequestBody Wishlist wishlist) {
        return new ResponseEntity<>(wlService.create(wishlist), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> updateWishlist(@PathVariable long id, @RequestBody Wishlist wl) {
        return wlService.findById(id)
            .map(wishlist -> {
                String name = wl.getName();
                String userID = wl.getUserId();
                Optional<User> user = userService.findById(userID);
                if (name != null) {
                    wishlist.setName(name);
                }
                if (user.isPresent()) {
                    wishlist.setUser(user.get());
                }
                return new ResponseEntity<>(wlService.update(wishlist), HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @DeleteMapping
    public ResponseEntity<List<Wishlist>> deleteAllWishlists() {
        wlService.deleteAll();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Wishlist> deleteWishlist(@PathVariable long id) {
        return wlService.deleteById(id)
            .map(deletedWishlist -> new ResponseEntity<>(deletedWishlist, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

}
