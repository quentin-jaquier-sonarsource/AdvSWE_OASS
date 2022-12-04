package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResult;
import coms.w4156.moviewishlist.models.watchMode.WatchModeNetwork;
import coms.w4156.moviewishlist.services.RatingService;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.services.WishlistService;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
     * @return The Client object
     */
    @QueryMapping
    public Optional<Client> client(Authentication authentication) {
        String clientEmail = authentication.getName();
        return clientService.findByEmail(clientEmail);
    }

    /**
     * Fetch all profiles in the database.
     *
     * @return List of Profile objects
     */
    @QueryMapping
    public Collection<Profile> profiles(Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return new ArrayList<Profile>();
        }

        return profileService.getAllForClient(client.get().getId());
    }

    /**
     * Fetch a profile by id
     *
     * @param id - The id of the profile
     * @return The Profile object
     */
    @QueryMapping
    public Optional<Profile> profileById(@Argument final String id, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(Long.parseLong(id));
        if (!profile.isPresent() || profile.get().getClientId() != client.get().getId()) {
            return null;
        }

        return profile;
    }

    /**
     * Fetch a profile by name.
     *
     * @param name - The name of the profile
     * @return The Profile object
     */
    @QueryMapping
    public Optional<Profile> profileByName(@Argument final String name, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findByName(name);
        if (!profile.isPresent() || profile.get().getClientId() != client.get().getId()) {
            return null;
        }

        return profile;
    }


    /**
     * Fetch all ratings in the database.
     *
     * @return List of ratings objects
     */
    @QueryMapping
    public Collection<Rating> ratings(Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return new ArrayList<Rating>();
        }

        return ratingService.getAllForClient(client.get().getId());
    }

    /**
     * Fetch a rating by id.
     *
     * @param id - The id of the rating
     *
     * @return Ratings objects
     */
    @QueryMapping
    public Optional<Rating> ratingById(@Argument final Long id, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Rating> rating = ratingService.findById(id);
        if (!rating.isPresent() || rating.get().getClientId() != client.get().getId()) {
            return null;
        }

        return rating;
    }

    /**
     * Fetch a rating by the profile that gave the rating.
     *
     * @param profileId - The id of the profile that gave the rating
     *
     * @return Ratings objects
     */
    @QueryMapping
    public Collection<Rating> ratingsByProfile(@Argument final String profileId, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Profile> profile = profileService.findById(Long.parseLong(profileId));
        if (!profile.isPresent() || profile.get().getClientId() != client.get().getId()) {
            return null;
        }

        return ratingService.getAllForProfileId(Long.parseLong(profileId));
    }

    /**
     * Fetch a rating by the movie for which the rating was given.
     *
     * @param movieId - The id of the movie for which the rating was given
     *
     * @return List of Ratings objects
     */
    @QueryMapping
    public Collection<Rating> ratingsByMovie(@Argument final String movieId, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        return ratingService.getByMovieIdForClient(Long.parseLong(movieId), client.get().getId());
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
     * Fetch a movie by Id
     *
     * @return The Movie object
     */
    @QueryMapping
    public Optional<Movie> movieById(@Argument final Long id) {
        return movieService.findById(id);
    }

    @QueryMapping
    public Collection<Movie> moviesByGenre(
            @Argument final String id,
            @Argument final String genre
    ){
        return wishlistService.findById(Long.parseLong(id)).get().getMoviesByGenre(genre);
    }

    @QueryMapping
    public Collection<Movie> moviesByReleaseYear(
            @Argument final String id,
            @Argument final String release_year
    ){

        return wishlistService.findById(Long.parseLong(id)).get().getMoviesByReleaseYear(release_year);
    }

    @QueryMapping
    public Collection<Movie> moviesByRuntime(
            @Argument final String id,
            @Argument final int runtime
    ){

        return wishlistService.findById(Long.parseLong(id)).get().getMoviesByRuntime(runtime);
    }

    @QueryMapping
    public Collection<Movie> moviesByCriticScore(
            @Argument final String id,
            @Argument final int critic_score
    ){

        return wishlistService.findById(Long.parseLong(id)).get().getMoviesByCriticScore(critic_score);
    }

    /**
     * Fetch movie by name in the waishlist.
     *
     * @return List of User objects
     */
//    @QueryMapping
//    public Collection<Movie> movieByName(@Argument) {
//        //get wishlist by id

//        // find movie name inside the wishlist

//        //return wishlistService.get
//    }


    /**
     * Fetch a movie by title.
     * filter all movies by genre in a specific wishlist
     * @param id - Wishlist id
     * @param genre - Genre
     *
     * @return List of movies of that genre in the wishlist
     */
    @QueryMapping
    public Collection<TitleSearchResult> searchTitles(
        @Argument final String title
    ) {
        return watchModeService.getTitlesBySearch(title).results();
    }

    /**
     * Get the details for this title using the /title API.
     * @param searchResult - The search result to get the details for.
     * @param env - The GraphQL environment
     * @return - The title details
     */
    @SchemaMapping(typeName = "TitleSearchResult", field = "details")
    public TitleDetail getDetails(
        final TitleSearchResult searchResult,
        final DataFetchingEnvironment env
    ) {
        Set<String> knownFields = Set.of(
            "id",
            "title",
            "type",
            "year",
            "tmdbId",
            "tmdbType",
            "poster"
        );

        long unknownFields = env
            .getSelectionSet()
            .getFields()
            .stream()
            .filter(el -> !knownFields.contains(el.getName()))
            .count();

        Boolean includeSources = env.getSelectionSet().contains("sources");

        if (unknownFields > 0) {
            return watchModeService.getTitleDetail(
                searchResult.getId().toString(),
                includeSources
            );
        }

        TitleDetail detail = new TitleDetail();
        detail.setId(searchResult.getId());
        detail.setTitle(searchResult.getName());
        detail.setType(searchResult.getType());
        detail.setYear(searchResult.getYear());
        detail.setTmdbId(searchResult.getTmdbId());
        detail.setTmdbType(searchResult.getTmdbType());
        detail.setPoster(searchResult.getImageUrl());
        return detail;
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
     * Get title details by id.
     *
     * @param titledetail - The TitleDetail object
     * @param env - The DataFetchingEnvironment
     *
     * @return Details of the Title
     */
    @SchemaMapping(typeName = "TitleDetail", field = "similarTitles")
    public List<TitleDetail> similarTitles(
        final TitleDetail titledetail,
        final DataFetchingEnvironment env
    ) {
        DataFetchingFieldSelectionSet selectionSet = env.getSelectionSet();

        if (
            selectionSet.contains("id") && selectionSet.getFields().size() == 1
        ) {
            return titledetail
                .getSimilarTitlesIds()
                .stream()
                .map(id -> {
                    TitleDetail detail = new TitleDetail();
                    detail.setId(id);
                    return detail;
                })
                .toList();
        }

        Boolean hasSources = env.getSelectionSet().contains("sources");

        return titledetail
            .getSimilarTitlesIds()
            .stream()
            .map(id ->
                watchModeService.getTitleDetail(id.toString(), hasSources)
            )
            .toList();
    }

    /**
     * Fetch all wishlists
     *
     * @return List of the wishlists
     */
    @QueryMapping
    public Collection<Wishlist> wishlists(Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return new ArrayList<Wishlist>();
        }

        return wishlistService.getAllForClient(client.get().getId());
    }

    /**
     * Fetch a wishlist by id
     *
     * @return The Wishlist object
     */
    @QueryMapping
    public Optional<Wishlist> wishlistById(@Argument String id, Authentication authentication) {
        String clientEmail = authentication.getName();
        Optional<Client> client = clientService.findByEmail(clientEmail);
        if (!client.isPresent()) {
            return null;
        }

        Optional<Wishlist> wishlist = wishlistService.findById(Long.parseLong(id));
        if (!wishlist.isPresent()) {
            return null;
        }

        Profile profile = profileService.findById(wishlist.get().getProfileId()).get();
        if (profile.getClientId() != client.get().getId()) {
            return null;
        }

        return wishlist;
    }
}
