package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.WishlistService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MutationController {

    /**
     * Use dependency injection to inject various services.
     */
    @Autowired
    private ClientService clientService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private MovieService movieService;

    /**
     * Create a new client with the given email ID.
     *
     * @param email - Email ID of the client
     * @return the new client
     */
    @MutationMapping
    public Optional<Client> createClient(@Argument final String email) {
        Client client = new Client();
        client.setEmail(email);
        var newClient = clientService.create(client);
        return clientService.findById(newClient.getId());
        // TODO: handle error when client is not created
    }

    /**
     * Update a client with the given ID.
     * @param id - ID of the client to update
     * @param email - New email ID of the client
     * @return the updated client
     */
    @MutationMapping
    public Optional<Client> updateClient(
        @Argument final String id,
        @Argument final String email
    ) {
        return clientService
            .findById(Long.parseLong(id))
            .map(c -> {
                c.setEmail(email);
                return clientService.update(c);
            });
    }

    /**
     * Delete a client by ID.
     *
     * @param id - ID of the client to delete
     * @return the deleted client
     */
    @MutationMapping
    public Optional<Client> deleteClient(@Argument final String id) {
        return clientService.deleteById(Long.parseLong(id));
    }

    /**
     * Create a new profile with the given name.
     *
     * @param clientID - ID of the client to create the profile for
     * @param name - Name of the profile
     * @return the new profile
     */
    @MutationMapping
    public Profile createProfile(
        @Argument final String clientID,
        @Argument final String name
    ) {
        Client client = clientService.findById(Long.parseLong(clientID)).get();
        Profile profile = new Profile();
        profile.setName(name);
        profile.setClient(client);
        return profileService.create(profile);
    }

    /**
     * Update a profile with the given ID.
     *
     * @param id - id of the profile to update
     * @param name - New name of the profile
     * @return the updated profile
     */
    @MutationMapping
    public Optional<Profile> updateProfile(
        @Argument final String id,
        @Argument final String name
    ) {
        return profileService
            .findById(Long.parseLong(id))
            .map(profile -> {
                profile.setName(name);
                return profileService.update(profile);
            });
    }

    /**
     * Delete a profile by email.
     *
     * @param id - id of the profile to delete
     * @return the deleted profile
     */
    @MutationMapping
    public Optional<Profile> deleteProfile(@Argument final String id) {
        return profileService.deleteById(Long.parseLong(id));
    }

    /**
     * Create a new Wishlist for a profile.
     *
     * @param profileID - Email of the profile to create the wishlist for
     * @param wishlistName - Name of the wishlist
     * @return the new wishlist
     */
    @MutationMapping
    public Wishlist createWishlist(
        @Argument final String profileID,
        @Argument final String wishlistName
    ) {
        return wishlistService.create(
            Wishlist
                .builder()
                .name(wishlistName)
                .profile(
                    profileService.findById(Long.parseLong(profileID)).get()
                )
                .build()
        );
    }

    /**
     * Update a wishlist with the given ID.
     *
     * @param id - ID of the wishlist to update
     * @param name - New name of the wishlist
     * @return the updated wishlist
     */
    @MutationMapping
    public Optional<Wishlist> updateWishlist(
        @Argument final String id,
        @Argument final String name
    ) {
        return wishlistService
            .findById(Long.parseLong(id))
            .map(w -> {
                w.setName(name);
                return wishlistService.update(w);
            });
    }

    /**
     * Delete a wishlist by ID.
     *
     * @param id - ID of the wishlist to delete
     * @return the deleted wishlist
     */
    @MutationMapping
    public Optional<Wishlist> deleteWishlist(@Argument final String id) {
        return wishlistService.deleteById(Long.parseLong(id));
    }

    /**
     * Create a new movie with the given title.
     *
     * @param wishlistID - ID of the wishlist to create the movie for
     * @param movieID - ID of the movie
     * @return the new movie
     */
    @MutationMapping
    public Movie addMovieToWishlist(
        @Argument final String wishlistID,
        @Argument final String movieID
    ) {
        var wishlist = wishlistService
            .findById(Long.parseLong(wishlistID))
            .get();

        return movieService
            .findById(Long.parseLong(movieID))
            .map(m -> {
                Long matchingWishlists = m
                    .getWishlists()
                    .stream()
                    .filter(w -> w.getId().equals(wishlist.getId()))
                    .count();

                if (matchingWishlists == 0) {
                    m.getWishlists().add(wishlist);
                    return movieService.update(m);
                }
                return m;
            })
            .orElseGet(() ->
                movieService.create(
                    Movie
                        .builder()
                        .id(Long.parseLong(movieID))
                        .wishlists(List.of(wishlist))
                        .build()
                )
            );
    }
}
