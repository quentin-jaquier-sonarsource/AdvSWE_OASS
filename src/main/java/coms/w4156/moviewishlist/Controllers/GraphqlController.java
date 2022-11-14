package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.User;
import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResult;
import coms.w4156.moviewishlist.models.watchMode.WatchModeNetwork;
import coms.w4156.moviewishlist.models.watchMode.WatchModeSource;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.UserService;
import coms.w4156.moviewishlist.services.WatchModeService;
import graphql.schema.DataFetchingEnvironment;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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
     * Get all WatchMode sources.
     *
     * @return List of User objects
     */
    @QueryMapping
    public Collection<WatchModeSource> sources() {
        return watchModeService.getAllSources();
    }

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
}
