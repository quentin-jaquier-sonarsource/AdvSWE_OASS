package coms.w4156.moviewishlist.serviceTests;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.repositories.RatingRepository;
import coms.w4156.moviewishlist.services.RatingService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

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
    @DisplayName("JUnit test for findAll() for ratings")
    void testFindAll() {
        List<Rating> ratings = new ArrayList<Rating>();
        ratings.add( Rating.builder().id(Long.valueOf("8")).rating(3.0).review("Average").build());
        ratings.add( Rating.builder().id(Long.valueOf("9")).rating(1.5).review("GreatMovie").build());
        Mockito
                .when(ratingService.findAll())
                .thenReturn(ratings);

        Iterable<Rating> returnedRatings = ratingService.findAll();
        Assertions.assertNotNull(returnedRatings);
        Assertions.assertIterableEquals(returnedRatings, ratings);
    }

    @Test
    @DisplayName("JUnit test for findAll() for ratings")
    void testGetAllForClient() {
        List<Rating> ratings = new ArrayList<Rating>();
        List<Rating> expectedRatings = new ArrayList<Rating>();

        Client client1 = new Client();
        Client client2 = new Client();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();

        client1.setId(1L);
        client2.setId(2L);

        profile1.setClient(client1);
        profile2.setClient(client2);

        Rating rating = new Rating();
        rating.setProfile(profile1);

        Rating rating2 = Rating.builder()
                .id(8L).rating(3.0).review("Average").profile(profile1).build();

        ratings.add(rating);
        ratings.add(rating2);
        ratings.add( Rating.builder().id(9L).rating(1.5).review("GreatMovie").profile(profile2).build());

        expectedRatings.add(rating);
        expectedRatings.add(rating2);


        Mockito
                .when(ratingRepository.findAll())
                .thenReturn(ratings);

        Iterable<Rating> returnedRatings = ratingService.getAllForClient(1L);
        Assertions.assertNotNull(returnedRatings);
        Assertions.assertIterableEquals(expectedRatings, returnedRatings);
    }

    /**
     * Tests that we can filter by both movie and client id.
     */
    @Test
    @DisplayName("JUnit test for findAll() for ratings")
    void testGetByMovieIdForClient() {

        Long movieIdWeCareAbout = 42L;
        Long clientIdWeCareAbout = 1L;

        // 3 ratings, 2 clients, 2 profiles, 3 movies.
        List<Rating> ratings = new ArrayList<Rating>();
        List<Rating> expectedRatings = new ArrayList<Rating>();

        Movie matrix = Movie.builder().id(1L).build();
        Movie lotr = Movie.builder().id(movieIdWeCareAbout).build();
        Movie avp = Movie.builder().id(666L).build();

        Client client1 = new Client();
        Client client2 = new Client();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();

        client1.setId(clientIdWeCareAbout);
        client2.setId(2L);

        profile1.setClient(client1);
        profile2.setClient(client2);

        // Wrong movie right profile
        Rating rating = new Rating();
        rating.setProfile(profile1);
        rating.setMovie(matrix);

        // Right movie right profile
        Rating rating2 = Rating.builder()
                .id(8L).rating(3.0).review("Average").profile(profile1)
                .movie(lotr).build();

        // Right movie wrong profile
        Rating rating3 = Rating.builder()
                        .id(55L).rating(5.0).review("Awesome").profile(profile2)
                        .movie(lotr).build();

        ratings.add(rating);
        ratings.add(rating2);
        ratings.add(rating3);

        // wrong movie wrong profile
        ratings.add( Rating.builder().id(9L).rating(1.5).review("GreatMovie").profile(profile2).movie(avp).build());

        // Only interested in reviews of lotr in profile 1
        expectedRatings.add(rating2);


        Mockito
                .when(ratingRepository.findAll())
                .thenReturn(ratings);

        Iterable<Rating> returnedRatings = ratingService.getByMovieIdForClient(movieIdWeCareAbout, clientIdWeCareAbout);
        Assertions.assertNotNull(returnedRatings);
        Assertions.assertIterableEquals(expectedRatings, returnedRatings);
    }

    @Test
    @DisplayName("JUnit test for findAll() for ratings")
    void testGetAllForProfileId() {
        List<Rating> ratings = new ArrayList<Rating>();
        List<Rating> expectedRatings = new ArrayList<Rating>();

        Profile profile1 = Profile.builder().id(1L).build();
        Profile profile2 = Profile.builder().id(42L).build();

        Rating rating = new Rating();
        rating.setProfile(profile1);

        Rating rating2 = Rating.builder()
                .id(8L).rating(3.0).review("Average").profile(profile1).build();

        ratings.add(rating);
        ratings.add(rating2);
        ratings.add( Rating.builder().id(9L).rating(1.5).review("GreatMovie").profile(profile2).build());

        expectedRatings.add(rating);
        expectedRatings.add(rating2);


        Mockito
                .when(ratingRepository.findAll())
                .thenReturn(ratings);

        Iterable<Rating> returnedRatings = ratingService.getAllForProfileId(1L);
        Assertions.assertNotNull(returnedRatings);
        Assertions.assertIterableEquals(expectedRatings, returnedRatings);
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

    /**
     * In the above case there are no ratings with this id in the repository.
     * Now we test what happens when a rating is actually returned back.
     */
    @Test
    @DisplayName("JUnit test for deleteById() for ratings")
    void deleteByIdRatingPresentCase() {
        Rating rating = Rating.builder().id(Long.valueOf("12")).rating(1.0).review("Really Bad").build();

        Mockito
                .when(ratingRepository.findById(12L))
                .thenReturn(Optional.ofNullable(rating));

        ratingService.deleteById(rating.getId());
        verify(ratingRepository).findById(rating.getId());
        verify(ratingRepository).deleteById(rating.getId());
    }

}