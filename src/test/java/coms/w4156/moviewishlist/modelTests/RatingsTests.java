package coms.w4156.moviewishlist.modelTests;

import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

class RatingsTests {
    @Test
    void testToString() {
        Rating rating = new Rating();

        Assertions.assertEquals("Rating(id=null, profile=null, movie=null, review=null, rating=null)", rating.toString());
    }

    @Test
    void testEquals() {
        Rating rating = new Rating();
        Rating rating1 = new Rating();

        Assertions.assertEquals(rating, rating1);
    }

    @Test
    void testReviewRatingsConstructor() {
        String review = "Awesome";
        double rating2 = 5.0;

        Rating rating = new Rating(review, rating2);
        Rating rating1 = new Rating(review, rating2);

        Assertions.assertEquals(rating, rating1);
    }

    @Test
    void testProfileMovieRatingReviewConstructor() {
        Profile profile = new Profile();
        Movie movie = new Movie();
        String review = "Awesome";
        double rating2 = 5.0;

        Rating rating = new Rating(profile, movie, review, rating2);
        Rating rating1 = new Rating(profile, movie, review, rating2);

        Assertions.assertEquals(rating, rating1);
    }

    @Test
    void testGetProfileId() {

        long profId = 42L;
        Profile profile = Profile.builder().id(profId).build();
        long hostId = 1616666L;
        Movie movie = Movie.builder().id(hostId).build();
        String review = "Awesome";
        double rating2 = 5.0;

        Rating rating = new Rating(profile, movie, review, rating2);

        Assertions.assertEquals(profId, rating.getProfileId());
    }

    @Test
    void testGetMovieId() {

        long profId = 42L;
        Profile profile = Profile.builder().id(profId).build();
        long hostId = 1616666L;
        Movie movie = Movie.builder().id(hostId).build();
        String review = "Awesome";
        double rating2 = 5.0;

        Rating rating = new Rating(profile, movie, review, rating2);

        Assertions.assertEquals(hostId, rating.getMovieId());
    }
}
