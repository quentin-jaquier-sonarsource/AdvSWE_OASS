package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.WishlistService;
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

@RequestMapping(value = "/wishlists")
@RestController
public class WishlistController {

    /**
     * Use dependency injection to inject an object of the
     * WishlistService class.
     */
    @Autowired
    private WishlistService wlService;

    /**
     * Use dependency injection to inject an object of the
     * ProfileService class.
     */
    @Autowired
    private ProfileService profileService;

    /**
     * `/wishlists` will fetch a list of all wishlists in the database.
     *
     * @return a list of wishlist objects
     */
    @GetMapping
    public ResponseEntity<List<Wishlist>> getAllWishlists() {
        List<Wishlist> wlList = wlService.getAll();
        return new ResponseEntity<>(wlList, HttpStatus.OK);
    }

    /**
     * Get a particular wishlist to ID. If ID not found, HTTP 204: No Content.
     * @param id - ID of the wishlist to get
     * @return a single wishlist object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(
        @PathVariable final long id
    ) {
        return wlService
            .findById(id)
            .map(wishlist -> new ResponseEntity<>(wishlist, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * POST `/wishlists` will create a new wishlist. The fields for the
     * wishlist object must be passed in as the RequestBody as json.path.
     * @param wishlist - wishlist object to add to the database.
     * @return The wishlist object that was just created
     */
    @PostMapping
    public ResponseEntity<Wishlist> createWishlist(
        @RequestBody final Wishlist wishlist
    ) {
        if (wishlist.getName().isEmpty()
            || wishlist.getProfileId() == null
            || wishlist.getMovieIds() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(wlService.create(wishlist), HttpStatus.OK);
    }

    /**
     * PUT `/wishlists/{id}` updates an existing wishlist with the given ID.
     * The updated fields for the wishlist should be passed in as the JSON
     * Request Body.
     *
     * @param id - ID of the wishlist to update
     * @param wl - wishlist data for the updated wishlist.
     * @return The newly updated wishlist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Wishlist> updateWishlist(
        @PathVariable final long id,
        @RequestBody final Wishlist wl
    ) {
        return wlService
            .findById(id)
            .map(wishlist -> {
                String name = wl.getName();
                Long profileID = wl.getProfileId();
                Optional<Profile> profile = profileService.findById(profileID);
                if (name != null) {
                    wishlist.setName(name);
                }
                if (profile.isPresent()) {
                    wishlist.setProfile(profile.get());
                }
                return new ResponseEntity<>(
                    wlService.update(wishlist),
                    HttpStatus.OK
                );
            })
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * Delete all wishlists.
     * @return The list of wishlists that were just deleted
     */
    @DeleteMapping
    public ResponseEntity<List<Wishlist>> deleteAllWishlists() {
        wlService.deleteAll();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    /**
     * Delete a particular wishlist by ID.
     * @param id The ID of the wishlist to delete
     * @return the wishlist that was just deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Wishlist> deleteWishlist(
        @PathVariable final Long id
    ) {
        return wlService
            .deleteById(id)
            .map(wishlist -> new ResponseEntity<>(wishlist, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}
