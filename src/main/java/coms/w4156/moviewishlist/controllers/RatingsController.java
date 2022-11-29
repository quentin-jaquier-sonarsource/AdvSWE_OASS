package coms.w4156.moviewishlist.controllers;


import coms.w4156.moviewishlist.models.Ratings;
import coms.w4156.moviewishlist.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/rating")
@RestController
public class RatingsController {


    @Autowired
    private RatingService ratingService;


    @GetMapping
    public ResponseEntity<List<Ratings>> getAllRatings() {
        try {
            List<Ratings> allRatings = ratingService.getAll();
//            System.out.println("stopping here");
            return new ResponseEntity<>(allRatings, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Ratings> getRatingById(@PathVariable final Long id) {
        return ratingService.findById(id)
                .map(rating -> new ResponseEntity<>(rating, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }


    @PostMapping(consumes = "application/json")
    public ResponseEntity<Ratings> giveRating(@RequestBody Ratings ratings) {
        if (ratings.getId() == null
                || ratings.getMovieIds() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ratingService.create(ratings), HttpStatus.OK);
    }

}

