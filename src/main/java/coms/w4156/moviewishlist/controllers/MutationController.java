package coms.w4156.moviewishlist.controllers;

import java.util.List;
import java.util.Optional;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.models.Rating;
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

/* TODO: add authentication to the MutationController */

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

    @Autowired
    private WatchModeService watchModeService;

    /* TODO: this should also generate a JWT; for now we should be using the AuthController */
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

    /* TODO: remove because this is obsolete */
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
     * Delete the client that makes the call
     *
     * @return the deleted client
     */
    @MutationMapping
    public Optional<Client> deleteClient(Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        return clientService.deleteById(client.get().getId());
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
        @Argument final String name,
        Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        return profileService
            .findById(Long.parseLong(id))
            .filter(p -> p.getClientId() == client.get().getId())
            .map(profile -> {
                profile.setName(name);
                return profileService.update(profile);
            });
    }

    /**
     * Delete a profile by id.
     *
     * @param id - id of the profile to delete
     * @return the deleted profile
     */
    @MutationMapping
    public Optional<Profile> deleteProfile(@Argument final String id, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(Long.parseLong(id));
        if (!profile.isPresent() || profile.get().getClientId() != client.get().getId()) {
            return null;
        }

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
        @Argument final String wishlistName,
        Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(Long.parseLong(profileID));
        if (!profile.isPresent() || profile.get().getClientId() != client.get().getId()) {
            return null;
        }

        return wishlistService.create(
            Wishlist
                .builder()
                .name(wishlistName)
                .profile(profile.get())
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
        @Argument final String name,
        Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        return wishlistService.findById(Long.parseLong(id))
            .filter(w -> w.getClientId() == client.get().getId())
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
    public Optional<Wishlist> deleteWishlist(@Argument final String id, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Wishlist> wishlist = wishlistService.findById(Long.parseLong(id));
        if (!wishlist.isPresent() || wishlist.get().getClientId() != client.get().getId()) {
            return null;
        }

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
        @Argument final String movieID,
        Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Wishlist> wishlist = wishlistService.findById(Long.parseLong(wishlistID));
        if (!wishlist.isPresent() || wishlist.get().getClientId() != client.get().getId()) {
            return null;
        }

        var wishlistObj = wishlist.get();

        final String movie_name = watchModeService.getMovieName(movieID);
        final String movie_genre = watchModeService.getMovieGenre(movieID);
        final String movie_release_year = watchModeService.getMovieReleaseYear(movieID);
        final int movie_runtime = watchModeService.getMovieRuntime(movieID);
        final int critic_score = watchModeService.getMoviesByCriticScore(movieID);

        return movieService
            .findById(Long.parseLong(movieID))
            .map(m -> {
                Long matchingWishlists = m
                    .getWishlists()
                    .stream()
                    .filter(w -> w.getId().equals(wishlistObj.getId()))
                    .count();

                if (matchingWishlists == 0) {
                    m.getWishlists().add(wishlistObj);
                    return movieService.update(m);
                }
                return m;
            })
            .orElseGet(() ->
                movieService.create(
                    Movie
                        .builder()
                        .id(Long.parseLong(movieID))
                        .wishlists(List.of(wishlistObj))
                        .movie_name(movie_name)
                        .movie_gener(movie_genre)
                        .movie_release_year(movie_release_year)
                        .movie_runtime(movie_runtime)
                        .critic_score(critic_score)
                        .build()
                )
            );
    }


    /* TODO: create a movie if it does not exist */
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
            @Argument final Double rating,
            Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(Long.parseLong(profileId));
        if (!profile.isPresent() || profile.get().getClientId() != client.get().getId()) {
            return null;
        }

        Optional<Movie> movie = movieService.findById(Long.parseLong(movieId));
        if (!movie.isPresent()) {
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
            @Argument final Double rating,
            Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(Long.parseLong(profileId));
        if (!profile.isPresent() || profile.get().getClientId() != client.get().getId()) {
            return null;
        }

        Optional<Movie> movie = movieService.findById(Long.parseLong(movieId));
        if (!movie.isPresent()) {
            return null;
        }

        return ratingService
            .findById(Long.parseLong(id))
            .filter(r -> r.getClientId() == client.get().getId())
            .map(r -> {
                r.setProfile(profile.get());
                r.setRating(rating);
                r.setReview(review);
                r.setMovie(movie.get());
                return ratingService.update(r);
            });
    }


    /**
     * Delete a rating by ID.
     *
     * @param id - ID of the rating to delete
     * @return the deleted Rating object
     */
    @MutationMapping
    public Optional<Rating> deleteRating(@Argument final String id, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Rating> rating = ratingService.findById(Long.parseLong(id));
        if (!rating.isPresent() || rating.get().getClientId() != client.get().getId()) {
            return null;
        }

        return ratingService.deleteById(Long.parseLong(id));
    }

}
