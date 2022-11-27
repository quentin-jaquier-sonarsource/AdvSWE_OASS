package coms.w4156.moviewishlist.controllers;


import coms.w4156.moviewishlist.models.Ratings;
import coms.w4156.moviewishlist.services.MovieRatingService;
import coms.w4156.moviewishlist.services.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/rating")
@RestController
public class RatingsController {


    @Autowired
    private UserRatingService userRatingService;
    private MovieRatingService movieRatingService;


    @GetMapping
    public ResponseEntity<List<Ratings>> getAllUserRatings() {
        try{
            List<Ratings> allRatings = userRatingService.getAll();
            System.out.println("stopping here");
            return new ResponseEntity<>(allRatings, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Ratings> getUserRatingById(@PathVariable final Long id) {
        return userRatingService.findById(id)
                .map(rating -> new ResponseEntity<>(rating, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }


    @PostMapping(consumes="application/json")
    public ResponseEntity<Ratings> giveUserRating(@RequestBody Ratings ratings) {


        if ( ratings.getUserId().isEmpty()
                || ratings.getMovieIds() == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userRatingService.create(ratings), HttpStatus.OK);
    }


//    @GetMapping
//    public ResponseEntity<List<MovieRating>> getAllMovieRatings(){
//        return new ResponseEntity<>(movieRatingService.getAll(), HttpStatus.OK);
//    }


//    @GetMapping
//    public ResponseEntity<MovieRating> getMovieRatingById(){
//
//    }


}



// First order of business :
// CRUD for user's rating

// apis I want to create considering we have users not clients ;
// 1. (POST) Rating a movie + Writing a review (need movie ID and user ID)(case 1- no previous reviews by the user for that movie, so just add the review, case 2 - the user has already rated that movie before, we will update it instead of adding another entry)
// 2. (GET) Getting a movies's rating plus reviews (will need movie ID to fetch this)(case1 - ratings exist, return the reviews, case 2 - no reviews yet, return the string "no reviews yet")
// 3. (GET) Get how a particular user rated a movie (will need User ID and Movie ID to fetch this)(case 1 - user has rated this movie, return their review, case 2 - the user has not rated it before, return string "no review by this user found")
// 4. Delete rating by a user (will need user Id and the movie id) (case 1 - review exists, case 2 - review does not exist)
// 5. Updating the review posted by a user (user id plus movie id needed)

// Table 1 (user and the reviews they added) :
// review id, user id, movie id, rating, review

// Table 2 (movie and average rating) :
// movie id, list of strings, average rating, number of people who rated that movie

