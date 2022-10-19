package coms.w4156.moviewishlist.controllers;

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

import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.services.MovieService;

@RequestMapping(value = "/movies")
@RestController
public class MovieController {

    @Autowired
    MovieService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> getAll() {
        return new ResponseEntity<>(movieService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieService.findById(id)
            .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
    
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.create(movie), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie newData) {
        return movieService.findById(id)
            .map(movie -> {
                movie.setTitle(newData.getTitle());
                movieService.update(movie);
                return new ResponseEntity<>(movie, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @DeleteMapping
    public ResponseEntity<List<Movie>> deleteAllMovies() {
        movieService.deleteAll();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable Long id) {
        return movieService.deleteById(id)
            .map(deletedMovie -> new ResponseEntity<>(deletedMovie, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));   
    }

}
