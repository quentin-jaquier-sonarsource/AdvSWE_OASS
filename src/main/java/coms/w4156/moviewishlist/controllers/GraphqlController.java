package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.User;
import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResult;
import coms.w4156.moviewishlist.models.watchMode.WatchModeNetwork;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.UserService;
import coms.w4156.moviewishlist.services.WatchModeService;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GraphqlController {

    /**
     * Use dependency injection to inject an object of the UserService class.
     */
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private WatchModeService watchModeService;

    /**
     * Fetch all users in the database.
     *
     * @return List of User objects
     */
    @QueryMapping
    public Collection<User> users() {
        return userService.getAll();
    }

    /**
     * Fetch a user by email.
     *
     * @param email - The email address of the user
     *
     * @return List of User objects
     */
    @QueryMapping
    public Optional<User> userByEmail(@Argument final String email) {
        System.out.print(email);
        return userService.findById(email);
    }

    /**
     * Fetch all movies in the database.
     *
     * @return List of User objects
     */
    @QueryMapping
    public Collection<Movie> movies() {
        return movieService.getAll();
    }

    /**
     * Fetch a movie by id.
     *
     * @param title - The title of the movie
     *
     * @return List of User objects
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

    // /**
    //  * Get all WatchMode sources.
    //  *
    //  * @return List of User objects
    //  */
    // @QueryMapping
    // public Collection<WatchModeSource> sources() {
    //     return watchModeService.getAllSources();
    // }

    /**
     * Get all WatchMode networks.
     *
     * @return List of User objects
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
}
