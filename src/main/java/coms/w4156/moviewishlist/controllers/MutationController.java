package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.exceptions.ClientAlreadyExistsException;
import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.Role;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.security.jwt.JwtTokenUtil;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.RatingService;
import coms.w4156.moviewishlist.services.RoleService;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.services.WishlistService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtTokenUtil jwtUtility;

    /**
     * Create a new client with the given email.
     *
     * @param email - Email of the client
     * @return the JWT of the client
     */
    @MutationMapping
    public String createClient(@Argument final String email) {
        UserDetails clientDetails;

        try {
            clientDetails = clientService.createClientAndReturnDetails(email);
        } catch (ClientAlreadyExistsException e) {
            return "";
        }

        final String token = jwtUtility.generateToken(clientDetails);

        return token;
    }

    /**
     * Create an admin. Can only be used before any other client is created.
     *
     * @param email - Email of the admin
     * @return the JWT of the admin
     */
    @MutationMapping
    public String createAdmin(@Argument final String email) {
        UserDetails clientDetails;

        long count = clientService.getRepository().count();

        if (count != 0) {
            return "";
        }

        clientDetails = clientService.createAdminAndReturnDetails(email);

        final String token = jwtUtility.generateToken(clientDetails);

        return token;
    }

    /**
     * Update a client with the given ID.
     * @param id - ID of the client to update
     * @param email - New email ID of the client
     * @return the updated client
     */
    @PreAuthorize("hasRole('ROLE_admin')")
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

    @PreAuthorize("hasRole('ROLE_admin')")
    @MutationMapping
    public Optional<Client> addRoleToClient(
        @Argument final String email,
        @Argument final String roleName
    ) {
        Role role = roleService.findByName(roleName);

        return clientService
            .findByEmail(email)
            .map(c -> {
                c.addRole(role);
                return clientService.update(c);
            });
    }

    /**
     * Delete the client that makes the call.
     *
     * @param authentication The authentication object
     * @return the deleted client
     */
    @MutationMapping
    public Optional<Client> deleteClient(final Authentication authentication) {
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
     * @param clientID - ID of the client to create the profile for
     * @param name - Name of the profile
     * @param authentication The authentication object
     * @return the new profile
     */
    @MutationMapping
    public Optional<Profile> createProfile(
        @Argument final String clientID,
        @Argument final String name,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();

        return clientService
            .findByEmail(clientEmail)
            .map(client -> {
                Profile profile = new Profile();
                profile.setName(name);
                profile.setClient(client);
                return profileService.create(profile);
            });
    }

    /**
     * Update a profile with the given ID.
     *
     * @param id - id of the profile to update
     * @param name - New name of the profile
     * @param authentication The authentication object
     * @return the updated profile
     */
    @MutationMapping
    public Optional<Profile> updateProfile(
        @Argument final String id,
        @Argument final String name,
        final Authentication authentication
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
     * @param authentication The authentication object
     * @return the deleted profile
     */
    @MutationMapping
    public Optional<Profile> deleteProfile(
        @Argument final String id,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(Long.parseLong(id));
        if (
            !profile.isPresent() ||
            profile.get().getClientId() != client.get().getId()
        ) {
            return null;
        }

        return profileService.deleteById(Long.parseLong(id));
    }

    /**
     * Create a new Wishlist for a profile.
     *
     * @param profileID - Email of the profile to create the wishlist for
     * @param wishlistName - Name of the wishlist
     * @param authentication The authentication object
     * @return the new wishlist
     */
    @MutationMapping
    public Wishlist createWishlist(
        @Argument final String profileID,
        @Argument final String wishlistName,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(
            Long.parseLong(profileID)
        );
        if (
            !profile.isPresent() ||
            profile.get().getClientId() != client.get().getId()
        ) {
            return null;
        }

        return wishlistService.create(
            Wishlist.builder().name(wishlistName).profile(profile.get()).build()
        );
    }

    /**
     * Update a wishlist with the given ID.
     *
     * @param id - ID of the wishlist to update
     * @param name - New name of the wishlist
     * @param authentication The authentication object
     * @return the updated wishlist
     */
    @MutationMapping
    public Optional<Wishlist> updateWishlist(
        @Argument final String id,
        @Argument final String name,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        return wishlistService
            .findById(Long.parseLong(id))
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
     * @param authentication The authentication object
     * @return the deleted wishlist
     */
    @MutationMapping
    public Optional<Wishlist> deleteWishlist(
        @Argument final String id,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Wishlist> wishlist = wishlistService.findById(
            Long.parseLong(id)
        );
        if (
            !wishlist.isPresent() ||
            wishlist.get().getClientId() != client.get().getId()
        ) {
            return null;
        }

        return wishlistService.deleteById(Long.parseLong(id));
    }

    /**
     * Create a new movie with the given title.
     *
     * @param wishlistID - ID of the wishlist to create the movie for
     * @param movieID - ID of the movie
     * @param authentication The authentication object
     * @return the new movie
     */
    @MutationMapping
    public Optional<Movie> addMovieToWishlist(
        @Argument final String wishlistID,
        @Argument final String movieID,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> maybeClient = clientService.findByEmail(clientEmail);
        if (!maybeClient.isPresent()) {
            return Optional.empty();
        }
        Client client = maybeClient.get();

        Optional<Wishlist> maybeWishlist = wishlistService.findById(
            Long.parseLong(wishlistID)
        );
        if (!maybeWishlist.isPresent()) {
            return Optional.empty();
        }
        Wishlist wishlist = maybeWishlist.get();
        if (wishlist.getClientId() != client.getId()) {
            return Optional.empty();
        }

        return movieService
            .findById(Long.parseLong(movieID))
            .map((Movie m) -> {
                Optional<Wishlist> containsWishlist = m
                    .getWishlists()
                    .stream()
                    .filter(w -> w.getId().equals(wishlist.getId()))
                    .findFirst();

                if (containsWishlist.isEmpty()) {
                    wishlist.getMovies().add(m);
                    wishlistService.update(wishlist);
                    m.getWishlists().add(wishlist);
                    return Optional.of(movieService.update(m));
                }

                return Optional.of(m);
            })
            .orElseGet(() -> {
                TitleDetail details = watchModeService.getTitleDetail(
                    movieID,
                    false
                );

                if (details == null) {
                    return Optional.empty();
                }

                Movie newMovie = new Movie();
                newMovie.setId(Long.parseLong(movieID));
                newMovie.setWishlists(List.of(wishlist));
                newMovie.setTitle(details.getTitle());
                newMovie.setGenres(details.getGenreNames());
                newMovie.setReleaseYear(details.getYear());
                newMovie.setRuntimeMinutes(details.getRuntimeMinutes());
                newMovie.setCriticScore(details.getCriticScore());
                newMovie = movieService.create(newMovie);

                wishlist.getMovies().add(newMovie);
                wishlistService.update(wishlist);

                return Optional.of(newMovie);
            });
    }

    /* TODO: create a movie if it does not exist */
    /**
     * Create a new Rating for a profile and movie.
     *
     * @param profileId - ID of the profile to create the rating for
     * @param movieId - ID of the movie for which the rating is given
     * @param review - Comment left with the numerical rating
     * @param rating - The numerical rating given to a movie
     * @param authentication The authentication object
     * @return the new ratings object
     */
    @PreAuthorize("hasRole('ROLE_rating')")
    @MutationMapping
    public Rating createRating(
        @Argument final String profileId,
        @Argument final String movieId,
        @Argument final String review,
        @Argument final Double rating,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(
            Long.parseLong(profileId)
        );
        if (
            !profile.isPresent() ||
            profile.get().getClientId() != client.get().getId()
        ) {
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
     * @param authentication The authentication object
     * @return the updated ratings object
     */
    @PreAuthorize("hasRole('ROLE_rating')")
    @MutationMapping
    public Optional<Rating> updateRating(
        @Argument final String id,
        @Argument final String profileId,
        @Argument final String movieId,
        @Argument final String review,
        @Argument final Double rating,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(
            Long.parseLong(profileId)
        );
        if (
            !profile.isPresent() ||
            profile.get().getClientId() != client.get().getId()
        ) {
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
     * @param authentication The authentication object
     * @return the deleted Rating object
     */
    @PreAuthorize("hasRole('ROLE_rating')")
    @MutationMapping
    public Optional<Rating> deleteRating(
        @Argument final String id,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Rating> rating = ratingService.findById(Long.parseLong(id));
        if (
            !rating.isPresent() ||
            rating.get().getClientId() != client.get().getId()
        ) {
            return null;
        }

        return ratingService.deleteById(Long.parseLong(id));
    }
}
