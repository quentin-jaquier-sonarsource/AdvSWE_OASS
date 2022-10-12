package coms.w4156.moviewishlist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.WishlistService;

@RestController
public class WishlistController {

    @Autowired
    WishlistService wlService;
    
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody Wishlist createNewWishlist(@RequestParam("name") String name, @RequestParam("userId") Long userId) {
        Wishlist wl = new Wishlist(name , userId);

        return wl;
    }

    @GetMapping(value = "/{id}")
    public @ResponseBody Wishlist getWishlistById(@PathVariable Long id) {
        return wlService.findById(id);
    }
}
