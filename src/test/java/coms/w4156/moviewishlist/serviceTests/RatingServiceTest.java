package coms.w4156.moviewishlist.serviceTests;

import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.repositories.RatingRepository;
import coms.w4156.moviewishlist.services.RatingService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {


    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;


    @Test
    @DisplayName("JUnit test for getAll() for ratings")
    void getAll() {
        List<Rating> ratings = new ArrayList<Rating>();
        ratings.add( Rating.builder().id(Long.valueOf("8")).rating(3.0).review("Average").build());
        ratings.add( Rating.builder().id(Long.valueOf("9")).rating(1.5).review("GreatMovie").build());
        Mockito
                .when(ratingRepository.findAll())
                .thenReturn(ratings);

        List<Rating> returnedRatings = ratingService.getAll();
        Assertions.assertNotNull(returnedRatings);
        Assertions.assertEquals(returnedRatings.size(), ratings.size());
    }

    @Test
    @DisplayName("JUnit test for getAll() for ratings : negative scenario")
    void getAllReturningEmptyList () {
        Mockito
                .when(ratingRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Rating> returnedRatings = ratingService.getAll();
        Assertions.assertEquals(returnedRatings.size(), 0);
    }

    @Test
    @DisplayName("JUnit test for findById() for ratings")
    void findById() {
        Rating rating = Rating.builder().id(Long.valueOf("11")).rating(3.0).review("Not Bad").build();
        Mockito
                .when(ratingRepository.findById(Long.valueOf("11")))
                .thenReturn(Optional.of(rating));
        Rating returnedRating = ratingService.findById(Long.valueOf("11")).get();
        Assertions.assertNotNull(returnedRating);
    }

    @Test
    @DisplayName("JUnit test for findById() for ratings : negative case")
    void findByIdNotFound() {
        Mockito
                .when(ratingRepository.findById(Long.valueOf("11")))
                .thenReturn(null);
        Optional<Rating> returnedRating = ratingService.findById(Long.valueOf("11"));
        Assertions.assertNull(returnedRating);
    }

    @Test
    @DisplayName("JUnit test for create() for ratings")
    void create() {
        Rating rating = Rating.builder().id(Long.valueOf("12")).rating(1.0).review("Really Bad").build();
        Mockito
                .when(ratingRepository.save(rating))
                .thenReturn(rating);
        Rating returnedRating = ratingService.create(rating);
        Assertions.assertNotNull(returnedRating);
    }

    @Test
    @DisplayName("JUnit test for update() for ratings")
    void update() {
        Rating rating = Rating.builder().id(Long.valueOf("12")).rating(1.0).review("Really Bad").build();
        Mockito
                .when(ratingRepository.save(rating))
                .thenReturn(rating);
        Rating updatedRating = ratingService.update(rating);
        Assertions.assertNotNull(updatedRating);
    }

    @Test
    @DisplayName("JUnit test for deleteAll() for ratings")
    void deleteAll() {
        ratingService.deleteAll();
        verify(ratingRepository).deleteAll();
    }

    @Test
    @DisplayName("JUnit test for deleteById() for ratings")
    void deleteById() {
        Rating rating = Rating.builder().id(Long.valueOf("12")).rating(1.0).review("Really Bad").build();
        ratingService.deleteById(rating.getId());
        verify(ratingRepository).findById(rating.getId());
    }
}