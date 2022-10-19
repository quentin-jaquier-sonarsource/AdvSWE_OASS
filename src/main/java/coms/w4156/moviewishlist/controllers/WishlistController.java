package coms.w4156.moviewishlist.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.WishlistService;

@RequestMapping(value = "/wishlists")
@RestController
public class WishlistController {

    @Autowired
    WishlistService wlService;

    @GetMapping("/")
    public ResponseEntity<List<Wishlist>> getAllWishlists() {
        List<Wishlist> wlList = wlService.getAll();

        if (wlList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(wlList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable String id) {
        Long idLong = Long.parseLong(id, 10);

        Optional<Wishlist> wl = wlService.findById(idLong);

        if (!wl.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Wishlist _wl = wl.get();
            return new ResponseEntity<>(_wl, HttpStatus.OK);
        }
    }
    
    @PostMapping("/")
    public ResponseEntity<Wishlist> createWishlist(@RequestParam("name") String name, @RequestParam("userId") String userId) {
        Long userIdLong = Long.parseLong(userId, 10);
    
        Wishlist wl = wlService.create(name, userIdLong);
        return new ResponseEntity<>(wl, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> updateWishlist(@PathVariable String id, @RequestBody Wishlist wl) {
        Long idLong = Long.parseLong(id, 10);

        Optional<Wishlist> wlData = wlService.findById(idLong);

        if (!wlData.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Wishlist _wl = wlData.get();
            _wl.setName(wl.getName());
            _wl.setUserId(wl.getUserId());
            return new ResponseEntity<>(_wl, HttpStatus.OK);
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<HttpStatus> deleteAllWishlists() {
        try {
            wlService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteWishlist(@PathVariable String id) {
        Long idLong = Long.parseLong(id, 10);

        try {
            wlService.deleteById(idLong);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    
}
