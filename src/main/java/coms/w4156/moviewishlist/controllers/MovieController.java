package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.services.MovieService;
import java.util.ArrayList;
import java.util.List;
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

@RequestMapping(value = "/movies")
@RestController
public class MovieController {

    /**
     * Use dependency injection to inject an object of the MovieService class.
     */
    @Autowired
    private MovieService movieService;

    /**
     * `/movies` will fetch a list of all movies in the database.
     *
     * @return a list of movie objects
     */
    @GetMapping
    public ResponseEntity<List<Movie>> getAll() {
        return new ResponseEntity<>(movieService.getAll(), HttpStatus.OK);
    }

    /**
     * Get a particular movie to ID. If ID not found, HTTP 204: No Content.
     *
     * @param id - ID of the movie to get
     * @return a single movie object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable final Long id) {
        return movieService
            .findById(id)
            .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * POST `/movies` will create a new movie. The fields for the movie object
     * must be passed in as the RequestBody as json.path.
     *
     * @param movie - Movie object to add to the database.
     * @return The movie object that was just created
     */
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody final Movie movie) {
        if (movie.getTitle().isEmpty() || movie.getReleaseYear() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(movieService.create(movie), HttpStatus.OK);
    }

    /**
     * PUT `/movies/{id}` will update an existing movie with the given ID.
     * The updated fields for the movie should be passed in as the JSON Request
     * Body.
     *
     * @param id      - ID of the movie to update
     * @param newData - Movie data for the updated movie
     * @return The newly updated movie
     */
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
        @PathVariable final Long id,
        @RequestBody final Movie newData
    ) {
        return movieService
            .findById(id)
            .map(movie -> {
                movie.setTitle(newData.getTitle());
                movieService.update(movie);
                return new ResponseEntity<>(movie, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * Delete all movies.
     *
     * @return The list of movies that were just deleted
     */
    @DeleteMapping
    public ResponseEntity<List<Movie>> deleteAllMovies() {
        movieService.deleteAll();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    /**
     * Delete a particular movie by ID.
     *
     * @param id The ID of the movie to delete
     * @return the movie that was just deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable final Long id) {
        return movieService
            .deleteById(id)
            .map(deletedMovie ->
                new ResponseEntity<>(deletedMovie, HttpStatus.OK)
            )
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}
