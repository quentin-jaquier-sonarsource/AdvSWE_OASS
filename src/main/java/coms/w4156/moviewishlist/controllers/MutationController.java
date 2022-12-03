package coms.w4156.moviewishlist.controllers;

import java.util.List;
import java.util.Optional;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.services.RatingService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private RatingService ratingService;

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
     * @param name - Name of the profile
     * @return the new profile
     */
    @MutationMapping
    public Profile createProfile(
        @Argument final String name,
        Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Profile profile = new Profile();
        profile.setName(name);
        profile.setClient(client.get());
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


    /**
     * Create a new Rating for a profile and movie.
     *
     * @param profileId - ID of the profile to create the rating for
     * @param movieId - ID of the movie for which the rating is given
     * @param review - Comment left with the numerical rating
     * @param rating - The numerical rating given to a movie
     * @return the new ratings object
     */
    @MutationMapping
    public Rating createRating(
            @Argument final String profileId,
            @Argument final String movieId,
            @Argument final String review,
            @Argument final Double rating
    ) {
        System.out.println("createRating");
        Optional<Profile> profile = profileService.findById(Long.parseLong(profileId));
        if (!profile.isPresent()) {
            System.out.println("profile not present");
            return null;
        }

        Optional<Movie> movie = movieService.findById(Long.parseLong(movieId));
        if (!movie.isPresent()) {
            System.out.println("movie is not present");
            return null;
        }

        return ratingService.create(
                Rating
                        .builder()
                        .profile(profile.get())
                        .movie(movie.get())
                        .review(review)
                        .rating(rating)
                        .build()
        );
    }


    /**
     * Update a rating with the given ID.
     *
     * @param id - ID of the rating to update
     * @param profileId - ID of the profile to update the rating for
     * @param movieId - ID of the movie for which the rating is given
     * @param review - Comment left with the numerical rating
     * @param rating - The numerical rating given to a movie
     * @return the updated ratings object
     */
    @MutationMapping
    public Optional<Rating> updateRating(
            @Argument final String id,
            @Argument final String profileId,
            @Argument final String movieId,
            @Argument final String review,
            @Argument final Double rating
    ) {
        return ratingService
                .findById(Long.parseLong(id))
                .map(w -> {
                    w.setProfile(profileService.findById(
                            Long.parseLong(profileId)).get());
                    w.setRating(rating);
                    w.setReview(review);
                    w.setMovie(movieService.findById(
                            Long.parseLong(movieId)).get());
                    return ratingService.update(w);
                });
    }


    /**
     * Delete a rating by ID.
     *
     * @param id - ID of the rating to delete
     * @return the deleted Rating object
     */
    @MutationMapping
    public Optional<Rating> deleteRating(@Argument final String id) {
        return ratingService.deleteById(Long.parseLong(id));
    }

}
