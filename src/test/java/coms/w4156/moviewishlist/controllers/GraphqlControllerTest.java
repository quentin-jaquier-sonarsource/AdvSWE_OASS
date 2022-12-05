package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;


@TestInstance(Lifecycle.PER_CLASS)
@GraphQlTest(GraphqlController.class)
class GraphqlControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @MockBean
    ClientService clientService;

    @MockBean
    ProfileService profileService;

    @MockBean
    WishlistService wishlistService;

    @MockBean
    WatchModeService watchModeService;

    @MockBean
    MovieService movieService;

    @MockBean
    RatingService ratingService;

    Client client;

    @BeforeAll
    void createClient() {
        client = Client.builder().id(Long.valueOf("1")).email("user").build();
        Mockito
                .when(clientService.findByEmail("user"))
                .thenReturn(Optional.of(client));
    }

    @Test
    @WithMockUser
    void clientTest() {
        String query = """
                query {
                    client {
                        id,
                        email
                    }
                }
                """;
        graphQlTester.document(query)
            .execute()
            .path("client")
            .entity(Client.class)
            .satisfies(c -> {
                assertEquals(1, c.getId());
                assertEquals("user", c.getEmail());
            });
    }

    // @Test
    // void clientByIdTest() {
    //     String query = """
    //             query clientById($id: ID){
    //               clientById(id: $id){
    //                 email,
    //                 profiles{
    //                   id,
    //                   wishlists{
    //                     id,
    //                     email
    //                 }
    //             }
    //             """;
    //     graphQlTester.document(query)
    //             .execute()
    //             .path("client")
    //             .entity(Client.class)
    //             .satisfies(c -> {
    //                 assertEquals(1, c.getId());
    //                 assertEquals("user", c.getEmail());
    //             });
    // }

    @WithMockUser
    @Test
    void profilesTest() {
        Profile profile = Profile.builder().id(Long.valueOf(3)).name("profile1").client(client).build();
        Mockito
                .when(profileService.getAllForClient(client.getId()))
                .thenReturn(List.of(profile));

        String query = """
                query {
                  profiles{
                    id
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("profiles")
                .entityList(Profile.class)
                .satisfies(profiles -> {
                    assertEquals(1, profiles.size());
                });
    }

    @WithMockUser
    @Test
    void profileByIdTest(){
        Profile profile = Profile.builder().id(Long.valueOf(6)).name("profile6").client(client).build();
        Mockito
                .when(profileService.findById(Long.valueOf(6)))
                .thenReturn(Optional.ofNullable(profile));

        String query = """
            query {
              profileById(id: 6) {
                id
              }
            }
            """;

        graphQlTester.document(query)
                .execute()
                .path("profileById")
                .entity(Profile.class)
                .satisfies(p -> {
                    assertEquals(6, p.getId());
                });
    }


    @Test
    void moviesTest(){
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").genre("comedy").build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").genre("drama").build();
        Mockito
                .when(movieService.getAll())
                .thenReturn(List.of(movieOne, movieTwo));

        String query = """
                query {
                  movies{
                    id
                    name
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("movies")
                .entityList(Movie.class)
                .hasSize(2);
    }

    @Test
    @WithMockUser
    void moviesByGenreTest(){
        Profile profile = Profile.builder().id(Long.valueOf(3)).name("profile3").client(client).build();
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").genre("comedy").build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").genre("drama").build();

        Wishlist wishlist = Wishlist.builder()
            .id(Long.valueOf(4)).name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
                .when(wishlistService.findById(Long.valueOf(4)))
                .thenReturn(Optional.of(wishlist));

        String query = """
            query {
              moviesByGenre(wishlistID: 4, genre: \"drama\") {
                id,
                genre,
                runtime
              }
            }
            """;

        graphQlTester.document(query)
                .execute()
                .path("moviesByGenre")
                .entityList(Movie.class)
                .hasSize(1)
                .satisfies(movies -> {
                    assertEquals("drama", movies.get(0).getGenre());
                });
    }

    @Test
    @WithMockUser
    void moviesByReleaseYearTest() {
        Profile profile = Profile.builder().id(Long.valueOf(3)).name("profile3").client(client).build();
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").releaseYear("1999").build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").releaseYear("2004").build();

        Wishlist wishlist = Wishlist.builder()
            .id(Long.valueOf(4)).name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
                .when(wishlistService.findById(Long.valueOf(4)))
                .thenReturn(Optional.of(wishlist));


        String query = """
            query {
              moviesByReleaseYear(wishlistID: 4, releaseYear: \"2004\") {
                id,
                name,
                runtime,
                releaseYear
              }
            }
            """;

        graphQlTester.document(query)
                .execute()
                .path("moviesByReleaseYear")
                .entityList(Movie.class)
                .hasSize(1)
                .satisfies(movies -> {
                    assertEquals("2004", movies.get(0).getReleaseYear());
                });
    }

    @Test
    @WithMockUser
    void moviesByRuntimeTest() {
        Profile profile = Profile.builder().id(Long.valueOf(3)).name("profile3").client(client).build();
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").runtime(28).build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").runtime(123).build();

        Wishlist wishlist = Wishlist.builder()
            .id(Long.valueOf(4)).name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
                .when(wishlistService.findById(Long.valueOf(4)))
                .thenReturn(Optional.of(wishlist));

        String query = """
            query {
              moviesByRuntime(wishlistID: 4, runtime: 123) {
                id,
                name,
                runtime,
                releaseYear
              }
            }
            """;

        graphQlTester.document(query)
                .execute()
                .path("moviesByRuntime")
                .entityList(Movie.class)
                .hasSize(1)
                .satisfies(movies -> {
                    assertEquals(123, movies.get(0).getRuntime());
                });
    }

    @Test
    @WithMockUser
    void moviesByCriticScoreTest() {
        Profile profile = Profile.builder().id(Long.valueOf(3)).name("profile3").client(client).build();
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").criticScore(8).build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").criticScore(3).build();

        Wishlist wishlist = Wishlist.builder()
            .id(Long.valueOf(4)).name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
                .when(wishlistService.findById(Long.valueOf(4)))
                .thenReturn(Optional.of(wishlist));

        String query = """
            query {
              moviesByCriticScore(wishlistID: 4, criticScore: 8) {
                id,
                name,
                criticScore
              }
            }
            """;

        graphQlTester.document(query)
                .execute()
                .path("moviesByCriticScore")
                .entityList(Movie.class)
                .hasSize(1)
                .satisfies(movies -> {
                    assertEquals(8, movies.get(0).getCriticScore());
                });

    }

    /* TODO: This test is not relevant, we shouldn't be testing the watchmode API */
    // @Test
    // void searchTitlesTest() {
    //     String query = """
    //         query {
    //           searchTitles(title: \"Me Before You\") {
    //             id,
    //             details {
    //               criticScore
    //             }
    //           }
    //         }
    //         """;
    //     graphQlTester.document(query)
    //             .execute()
    //             .path("searchTitles")
    //             .entityList(TitleSearchResult.class)
    //             .satisfies(titleSearchResults -> {
    //                 assertEquals(2016, titleSearchResults.get(0).getYear());
    //             });

    // }

    /* TODO: This test is not relevant, we shouldn't be testing the watchmode API */
    // @Test
    // void titleDetailTest() {
    //     String query = """
    //             query titleDetail($id: ID!){
    //               titleDetail(id: $id){
    //                 id,
    //                 originalTitle,
    //                 releaseDate,
    //                 usRating,
    //                 genreNames
    //               }
    //             }
    //             """;
    //     graphQlTester.document(query)
    //             .variable("id", 1409931)
    //             .execute()
    //             .path("titleDetail")
    //             .entity(Movie.class)
    //             .satisfies(movie -> {
    //                 assertEquals("The Notebook", movie.getName());
    //             });
    // }

    @Test
    @WithMockUser
    void ratingsTest(){
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").criticScore(8).build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").criticScore(3).build();
        Profile profile = Profile.builder().id(Long.valueOf(3)).name("profile3").client(client).build();

        Rating ratingOne = Rating.builder()
              .id(Long.valueOf(1))
              .profile(profile)
              .rating(Double.valueOf(8))
              .review("Excellent")
              .movie(movieOne)
              .build();

        Rating ratingTwo = Rating.builder()
              .id(Long.valueOf(2))
              .profile(profile)
              .rating(Double.valueOf(3))
              .review("I hated it")
              .movie(movieTwo)
              .build();

        Mockito
                .when(ratingService.getAllForClient(client.getId()))
                .thenReturn(List.of(ratingOne, ratingTwo));


        String query = """
                query{
                  ratings{
                    review,
                    rating,
                    profile{
                      name
                    },
                    movie{
                      name,
                      criticScore
                    }
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("ratings")
                .entityList(Rating.class)
                .hasSize(2);
    }

    @Test
    @WithMockUser
    void ratingByIdTest(){
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").criticScore(8).build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").criticScore(3).build();
        Profile profile = Profile.builder().id(Long.valueOf(3)).name("profile3").client(client).build();

        Rating ratingOne = Rating.builder()
              .id(Long.valueOf(1))
              .profile(profile)
              .rating(Double.valueOf(8))
              .review("Excellent")
              .movie(movieOne)
              .build();

        Rating ratingTwo = Rating.builder()
              .id(Long.valueOf(2))
              .profile(profile)
              .rating(Double.valueOf(3))
              .review("I hated it")
              .movie(movieTwo)
              .build();

        Mockito
                .when(ratingService.findById(Long.valueOf(1)))
                .thenReturn(Optional.of(ratingOne));
        Mockito
                .when(ratingService.findById(Long.valueOf(2)))
                .thenReturn(Optional.of(ratingTwo));

        String query = """
            query {
              ratingById(id: 2) {
                id,
                review,
                rating
              }
            }
            """;

        graphQlTester.document(query)
                .execute()
                .path("ratingById")
                .entity(Rating.class)
                .satisfies(rating -> {
                    assertEquals(ratingTwo.getId(), rating.getId());
                    assertEquals(ratingTwo.getReview(), rating.getReview());
                });
    }

    @Test
    @WithMockUser
    void ratingsByProfileTest(){
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").criticScore(8).build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").criticScore(3).build();
        Profile profileOne = Profile.builder().id(Long.valueOf(3)).name("profile3").client(client).build();
        Profile profileTwo = Profile.builder().id(Long.valueOf(5)).name("profile5").client(client).build();

        Rating ratingOne = Rating.builder()
              .id(Long.valueOf(1))
              .profile(profileOne)
              .rating(Double.valueOf(8))
              .review("Excellent")
              .movie(movieOne)
              .build();

        Rating ratingTwo = Rating.builder()
              .id(Long.valueOf(2))
              .profile(profileTwo)
              .rating(Double.valueOf(3))
              .review("I hated it")
              .movie(movieTwo)
              .build();

        Mockito
                .when(ratingService.getAllForProfileId(profileOne.getId()))
                .thenReturn(List.of(ratingOne));
        Mockito
                .when(ratingService.getAllForProfileId(profileTwo.getId()))
                .thenReturn(List.of(ratingTwo));

        Mockito
                .when(profileService.findById(profileOne.getId()))
                .thenReturn(Optional.of(profileOne));
        Mockito
                .when(profileService.findById(profileTwo.getId()))
                .thenReturn(Optional.of(profileTwo));


        String query = """
            query {
              ratingsByProfile(profileId: \"5\") {
                id
                review,
                rating,
                profile {
                  id
                }
              }
            }
            """;

        graphQlTester.document(query)
                .execute()
                .path("ratingsByProfile")
                .entityList(Rating.class)
                .hasSize(1)
                .satisfies(ratings -> {
                    assertEquals(ratings.get(0).getId(), ratingTwo.getId());
                });
    }

    @Test
    @WithMockUser
    void ratingsByMovieTest(){
        Movie movieOne = Movie.builder().id(Long.valueOf(10)).name("Test").criticScore(8).build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(15)).name("Test2").criticScore(3).build();

        Profile profile = Profile.builder().id(Long.valueOf(3)).name("profile3").client(client).build();

        Rating ratingOne = Rating.builder()
              .id(Long.valueOf(1))
              .profile(profile)
              .rating(Double.valueOf(8))
              .review("Excellent")
              .movie(movieOne)
              .build();

        Rating ratingTwo = Rating.builder()
              .id(Long.valueOf(2))
              .profile(profile)
              .rating(Double.valueOf(3))
              .review("I hated it")
              .movie(movieOne)
              .build();

        Rating ratingThree = Rating.builder()
              .id(Long.valueOf(4))
              .profile(profile)
              .rating(Double.valueOf(5))
              .review("I would not see it again")
              .movie(movieOne)
              .build();


        Rating ratingOtherMovie = Rating.builder()
              .id(Long.valueOf(3))
              .profile(profile)
              .rating(Double.valueOf(5))
              .review("meh")
              .movie(movieTwo)
              .build();

        Mockito
              .when(ratingService.getByMovieIdForClient(movieOne.getId(), client.getId()))
              .thenReturn(List.of(ratingOne, ratingTwo, ratingThree));

        Mockito
              .when(ratingService.getByMovieIdForClient(movieTwo.getId(), client.getId()))
              .thenReturn(List.of(ratingOtherMovie));

        String query = """
            query {
              ratingsByMovie(movieId: \"10\") {
                review,
                rating,
                id,
              }
            }
            """;

        graphQlTester.document(query)
                .execute()
                .path("ratingsByMovie")
                .entityList(Rating.class)
                .hasSize(3);
    }
}