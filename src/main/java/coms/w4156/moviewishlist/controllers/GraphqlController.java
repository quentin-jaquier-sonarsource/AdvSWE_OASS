package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResult;
import coms.w4156.moviewishlist.models.watchMode.WatchModeNetwork;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.RatingService;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.services.WishlistService;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class GraphqlController {

    /**
     * Use dependency injection to inject an object of the profileService class.
     */
    @Autowired
    private ClientService clientService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private WatchModeService watchModeService;

    @Autowired
    private RatingService ratingService;

    /**
     * Fetch the client making the request.
     *
     * @param authentication The authentication object
     * @return The Client object
     */
    @QueryMapping
    public Client client(final Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (client.isPresent()) {
            return client.get();
        }
        throw new GraphQLException("ACCESS DENIED: You are not logged in.");
    }

    /**
     * Fetch all profiles in the database.
     *
     * @param authentication The authentication object
     * @return List of Profile objects
     */
    @QueryMapping
    public Collection<Profile> profiles(final Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            throw new GraphQLException("ACCESS DENIED: You are not logged in.");
        }

        Collection<Profile> profiles = profileService.getAllForClient(
            client.get().getId()
        );
        return profiles;
    }

    /**
     * Fetch a profile by id.
     *
     * @param id - The id of the profile
     * @param authentication - The authentication object
     * @return The Profile object
     */
    @QueryMapping
    public Optional<Profile> profileById(
        @Argument final String id,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (client.isEmpty()) {
            throw new GraphQLException("ACCESS DENIED: You are not logged in.");
        }

        Optional<Profile> profile = profileService.findById(Long.parseLong(id));
        if (
            !profile.isPresent() ||
            profile.get().getClientId() != client.get().getId()
        ) {
            throw new GraphQLException("NOT FOUND: Profile with given id.");
        }

        return profile;
    }

    /**
     * Fetch all ratings in the database.
     *
     * @param authentication - The authentication object
     * @return List of ratings objects
     */
    @PreAuthorize("hasRole('ROLE_rating')")
    @QueryMapping
    public Collection<Rating> ratings(final Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (client.isEmpty()) {
            throw new GraphQLException("ACCESS DENIED: You are not logged in.");
        }

        return ratingService.getAllForClient(client.get().getId());
    }

    /**
     * Fetch a rating by id.
     *
     * @param id - The id of the rating
     * @param authentication - The authentication object
     * @return Ratings objects
     */
    @PreAuthorize("hasRole('ROLE_rating')")
    @QueryMapping
    public Optional<Rating> ratingById(
        @Argument final Long id,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (client.isEmpty()) {
            throw new GraphQLException("ACCESS DENIED: You are not logged in.");
        }

        Optional<Rating> rating = ratingService.findById(id);
        if (
            rating.isEmpty() ||
            rating.get().getClientId() != client.get().getId()
        ) {
            throw new GraphQLException("NOT FOUND: Rating with given ID.");
        }

        return rating;
    }

    /**
     * Fetch a rating by the profile that gave the rating.
     *
     * @param profileId - The id of the profile that gave the rating
     * @param authentication - The authentication object
     * @return Ratings objects
     */
    @PreAuthorize("hasRole('ROLE_rating')")
    @QueryMapping
    public Collection<Rating> ratingsByProfile(
        @Argument final String profileId,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (client.isEmpty()) {
            throw new GraphQLException("ACCESS DENIED: You are not logged in.");
        }

        Optional<Profile> profile = profileService.findById(
            Long.parseLong(profileId)
        );
        if (
            profile.isEmpty() ||
            profile.get().getClientId() != client.get().getId()
        ) {
            throw new GraphQLException("NOT FOUND: Profile with given ID.");
        }

        return ratingService.getAllForProfileId(Long.parseLong(profileId));
    }

    /**
     * Fetch a rating by the movie for which the rating was given.
     *
     * @param movieId - The id of the movie for which the rating was given
     * @param authentication - The authentication object
     * @return List of Ratings objects
     */
    @PreAuthorize("hasRole('ROLE_rating')")
    @QueryMapping
    public Collection<Rating> ratingsByMovie(
        @Argument final String movieId,
        final Authentication authentication
    ) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (client.isEmpty()) {
            throw new GraphQLException("ACCESS DENIED: You are not logged in.");
        }

        return ratingService.getByMovieIdForClient(
            Long.parseLong(movieId),
            client.get().getId()
        );
    }

    /**
     * Fetch all movies in the database.
     *
     * @return List of Movie objects
     */
    @QueryMapping
    public Collection<Movie> movies() {
        return movieService.getAll();
    }

    /**
     * Fetch a movie by genre.
     *
     * @param id - The id of the wishlist.
     * @param genre - The genre of the movie
     * @return The Movie object
     */
    @QueryMapping
    public Collection<Movie> moviesByGenre(
        @Argument final String id,
        @Argument final String genre
    ) {
        return wishlistService
            .findById(Long.parseLong(id))
            .get()
            .getMoviesByGenre(genre);
    }

    /**
     * Fetch a movie by genre.
     *
     * @param id - The id of the wishlist.
     * @param releaseYear - The release year of the movie
     * @return The Movie object
     */
    @QueryMapping
    public Collection<Movie> moviesByReleaseYear(
        @Argument final String id,
        @Argument final Integer releaseYear
    ) {
        return wishlistService
            .findById(Long.parseLong(id))
            .get()
            .getMoviesByReleaseYear(releaseYear);
    }

    /**
     * Fetch a movie by genre.
     *
     * @param id - The id of the wishlist.
     * @param runtime - The runtime of the movie
     * @return The Movie object
     */
    @QueryMapping
    public Collection<Movie> moviesByRuntime(
        @Argument final String id,
        @Argument final int runtime
    ) {
        return wishlistService
            .findById(Long.parseLong(id))
            .get()
            .getMoviesByRuntime(runtime);
    }

    /**
     * Fetch a movie by genre.
     *
     * @param id - The id of the wishlist.
     * @param criticScore - The critic score of the movie
     * @return The Movie object
     */
    @QueryMapping
    public Collection<Movie> moviesByCriticScore(
        @Argument final String id,
        @Argument final int criticScore
    ) {
        return wishlistService
            .findById(Long.parseLong(id))
            .get()
            .getMoviesByCriticScore(criticScore);
    }

    /**
     * Fetch a movie by title.
     *
     * @param title - The title of the movie
     * @return List of movies of that genre in the wishlist
     */
    @QueryMapping
    public Collection<TitleSearchResult> searchTitles(
        @Argument final String title
    ) {
        return watchModeService.getTitlesBySearch(title).results();
    }

    /**
     * Get all WatchMode networks.
     *
     * @return List of Profile objects
     */
    @QueryMapping
    public Collection<WatchModeNetwork> networks() {
        return watchModeService.getAllNetworks();
    }

    /**
     * Get title details by id.
     *
     * @param id - The id of the movie
     * @param env - The DataFetchingEnvironment
     *
     * @return Details of the Title
     */
    @QueryMapping
    public TitleDetail titleDetail(
        @Argument final String id,
        final DataFetchingEnvironment env
    ) {
        Boolean includeSources = env.getSelectionSet().contains("sources");
        return watchModeService.getTitleDetail(id, includeSources);
    }

    /**
     * Get title details for a movie.
     *
     * @param movie - The local movie object
     * @param env - The DataFetchingEnvironment
     *
     * @return Details of the Title
     */
    @SchemaMapping(typeName = "Movie", field = "details")
    public TitleDetail getMovieDetails(
        final Movie movie,
        final DataFetchingEnvironment env
    ) {
        Boolean includeSources = env.getSelectionSet().contains("sources");
        return watchModeService.getTitleDetail(
            movie.getId().toString(),
            includeSources
        );
    }

    /**
     * Fetch all wishlists.
     *
     * @param authentication - The authentication object
     * @return List of the wishlists
     */
    @QueryMapping
    public Collection<Wishlist> wishlists(final Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (client.isEmpty()) {
            throw new GraphQLException("ACCESS DENIED: You are not logged in.");
        }

        return wishlistService.getAllForClient(client.get().getId());
    }
}
