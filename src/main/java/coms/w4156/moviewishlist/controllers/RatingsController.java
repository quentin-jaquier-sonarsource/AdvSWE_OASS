package coms.w4156.moviewishlist.controllers;


import coms.w4156.moviewishlist.models.Ratings;
import coms.w4156.moviewishlist.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequestMapping(value = "/rating")
@RestController
public class RatingsController {


    /**
     * Use dependency injection to inject an object of the
     * RatingService class.
     */
    @Autowired
    private RatingService ratingService;


    /**
     * `/rating` will fetch a list of all ratings in the database.
     *
     * @return a list of ratings objects
     */
    @GetMapping
    public ResponseEntity<List<Ratings>> getAllRatings() {
        try {
            List<Ratings> allRatings = ratingService.getAll();
            return new ResponseEntity<>(allRatings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Get a particular rating to ID. If ID not found, HTTP 204: No Content.
     * @param id - ID of the rating to get
     * @return a single rating object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ratings> getRatingById(@PathVariable final Long id) {
        return ratingService.findById(id)
                .map(rating -> new ResponseEntity<>(rating, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }



    /**
     * POST `/rating` will create a new rating. The fields for the
     * ratings object must be passed in as the RequestBody as json.path.
     * @param ratings - ratings object to add to the database.
     * @return The ratings object that was just created
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Ratings> giveRating(@RequestBody final Ratings ratings) {
        if (ratings.getId() == null
                || ratings.getMovieIds() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                ratingService.create(ratings),
                HttpStatus.OK);
    }

}

