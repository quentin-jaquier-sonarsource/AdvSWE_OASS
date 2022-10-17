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

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.WishlistService;

@RequestMapping(value = "/wishlists")
@RestController
public class WishlistController {

    @Autowired
    WishlistService wlService;

    @GetMapping
    public ResponseEntity<List<Wishlist>> getAllWishlists() {
        List<Wishlist> wlList = wlService.getAll();
        return new ResponseEntity<>(wlList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable long id) {
        Optional<Wishlist> wl = wlService.findById(id);

        if (!wl.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Wishlist _wl = wl.get();
            return new ResponseEntity<>(_wl, HttpStatus.OK);
        }
    }
    
    @PostMapping
    public ResponseEntity<Wishlist> createWishlist(@RequestBody Wishlist wishlist) {
        wlService.create(wishlist);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> updateWishlist(@PathVariable long id, @RequestBody Wishlist wl) {
        Optional<Wishlist> wlData = wlService.findById(id);

        if (wlData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Wishlist wishlist = wlData.get();
        wishlist.setName(wl.getName());
        wishlist.setUserId(wl.getUserId());
        wlService.update(wishlist);

        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<List<Wishlist>> deleteAllWishlists() {
        try {
            wlService.deleteAll();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Wishlist> deleteWishlist(@PathVariable long id) {
        try {
            Optional<Wishlist> wishlist = wlService.deleteById(id);
            if (wishlist.isPresent()) {
                return new ResponseEntity<>(wishlist.get(), HttpStatus.OK);    
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
