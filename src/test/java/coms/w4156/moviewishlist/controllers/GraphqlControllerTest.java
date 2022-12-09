package coms.w4156.moviewishlist.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResponse;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResult;
import coms.w4156.moviewishlist.models.watchMode.WatchModeNetwork;
import coms.w4156.moviewishlist.models.watchMode.WatchModeSource;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.RatingService;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.services.WishlistService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;

@GraphQlTest(GraphqlController.class)
@TestInstance(Lifecycle.PER_CLASS)
class GraphqlControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private WishlistService wishlistService;

    @MockBean
    private WatchModeService watchModeService;

    @MockBean
    private MovieService movieService;

    @MockBean
    private RatingService ratingService;

    private Client client;

    @BeforeEach
    void createClient() {
        client = Client.builder().id(Long.valueOf("1")).email("user").build();
        Mockito
            .when(clientService.findByEmail("user"))
            .thenReturn(Optional.of(client));
    }

    @Test
    @WithMockUser
    void clientTest() {
        String query =
            """
            query {
                client {
                    id
                    email
                }
            }
            """;
        graphQlTester
            .document(query)
            .execute()
            .path("client")
            .entity(Client.class)
            .satisfies(c -> {
                assertEquals(1, c.getId());
                assertEquals("user", c.getEmail());
            });
    }

    @WithMockUser
    @Test
    void profilesTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Mockito
            .when(profileService.getAllForClient(client.getId()))
            .thenReturn(List.of(profile));

        String query =
            """
                query {
                  profiles{
                    id,
                    client {
                      id
                    }
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("profiles")
            .entityList(Profile.class)
            .satisfies(profiles -> {
                assertEquals(1, profiles.get(0).getClient().getId());
                assertEquals(3, profiles.get(0).getId());
            });
    }

    @WithMockUser
    @Test
    void profileByIdTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(6))
            .name("profile6")
            .client(client)
            .build();
        Mockito
            .when(profileService.findById(Long.valueOf(6)))
            .thenReturn(Optional.ofNullable(profile));

        String query =
            """
            query {
              profileById(id: 6) {
                id
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("profileById")
            .entity(Profile.class)
            .satisfies(p -> {
                assertEquals(6, p.getId());
            });
    }

    @WithMockUser
    @Test
    void profileByIdFailTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(6))
            .name("profile6")
            .client(client)
            .build();
        Mockito
            .when(profileService.findById(Long.valueOf(6)))
            .thenReturn(Optional.ofNullable(profile));

        String query =
            """
            query {
              profileById(id: 6) {
                id
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("profileById")
            .entity(Profile.class)
            .satisfies(p -> {
                assertNotEquals(2, p.getId());
            });
    }

    @Test
    void moviesTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .genreString("comedy")
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .genreString("drama")
            .build();
        Mockito
            .when(movieService.getAll())
            .thenReturn(List.of(movieOne, movieTwo));

        String query =
            """
                query {
                  movies{
                    id
                    title
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("movies")
            .entityList(Movie.class)
            .hasSize(2);
    }

    @Test
    void moviesFailTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .genreString("comedy")
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .genreString("drama")
            .build();
        Mockito
            .when(movieService.getAll())
            .thenReturn(List.of(movieOne, movieTwo));

        String query =
            """
                query {
                  movies{
                    id
                    title
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("movies")
            .entityList(Movie.class)
            .satisfies(movies -> {
                assertFalse(movies.size() == 3);
            });
    }

    @Test
    @WithMockUser
    void moviesByGenreTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .genreString("comedy")
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .genreString("drama")
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.findById(Long.valueOf(4)))
            .thenReturn(Optional.of(wishlist));

        String query =
            """
            query {
              moviesByGenre(id: 4, genre: \"drama\") {
                id,
                genres,
                runtimeMinutes
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("moviesByGenre")
            .entityList(Movie.class)
            .hasSize(1)
            .satisfies(movies -> {
                assertEquals("drama", movies.get(0).getGenres().get(0));
            });
    }

    @Test
    @WithMockUser
    void moviesByGenreFailTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .genreString("comedy")
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .genreString("drama")
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.findById(Long.valueOf(4)))
            .thenReturn(Optional.of(wishlist));

        String query =
            """
            query {
              moviesByGenre(id: 4, genre: \"drama\") {
                id,
                genres,
                runtimeMinutes
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("moviesByGenre")
            .entityList(Movie.class)
            .hasSize(1)
            .satisfies(movies -> {
                assertNotEquals("comedy", movies.get(0).getGenres().get(0));
            });
    }

    @Test
    @WithMockUser
    void moviesByReleaseYearTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .releaseYear(1999)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .releaseYear(2004)
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.findById(Long.valueOf(4)))
            .thenReturn(Optional.of(wishlist));

        String query =
            """
            query {
              moviesByReleaseYear(id: 4, releaseYear: 2004) {
                id,
                title,
                runtimeMinutes,
                releaseYear
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("moviesByReleaseYear")
            .entityList(Movie.class)
            .hasSize(1)
            .satisfies(movies -> {
                assertEquals(2004, movies.get(0).getReleaseYear());
            });
    }

    @Test
    @WithMockUser
    void moviesByReleaseYearFailTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .releaseYear(1999)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .releaseYear(2004)
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.findById(Long.valueOf(4)))
            .thenReturn(Optional.of(wishlist));

        String query =
            """
            query {
              moviesByReleaseYear(id: 4, releaseYear: 2004) {
                id,
                title,
                runtimeMinutes,
                releaseYear
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("moviesByReleaseYear")
            .entityList(Movie.class)
            .hasSize(1)
            .satisfies(movies -> {
                assertNotEquals(2012, movies.get(0).getReleaseYear());
            });
    }

    @Test
    @WithMockUser
    void moviesByRuntimeTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .runtimeMinutes(28)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .runtimeMinutes(123)
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.findById(Long.valueOf(4)))
            .thenReturn(Optional.of(wishlist));

        String query =
            """
            query {
              moviesByRuntime(id: 4, runtime: 123) {
                id,
                title,
                runtimeMinutes,
                releaseYear
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("moviesByRuntime")
            .entityList(Movie.class)
            .hasSize(1)
            .satisfies(movies -> {
                assertEquals(123, movies.get(0).getRuntimeMinutes());
            });
    }

    @Test
    @WithMockUser
    void moviesByRuntimeFailTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .runtimeMinutes(28)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .runtimeMinutes(123)
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.findById(Long.valueOf(4)))
            .thenReturn(Optional.of(wishlist));

        String query =
            """
            query {
              moviesByRuntime(id: 4, runtime: 123) {
                id,
                title,
                runtimeMinutes,
                releaseYear
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("moviesByRuntime")
            .entityList(Movie.class)
            .hasSize(1)
            .satisfies(movies -> {
                assertNotEquals(160, movies.get(0).getRuntimeMinutes());
            });
    }

    @Test
    @WithMockUser
    void moviesByCriticScoreTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .criticScore(3)
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.findById(Long.valueOf(4)))
            .thenReturn(Optional.of(wishlist));

        String query =
            """
            query {
              moviesByCriticScore(id: 4, criticScore: 8) {
                id,
                title,
                criticScore
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("moviesByCriticScore")
            .entityList(Movie.class)
            .hasSize(1)
            .satisfies(movies -> {
                assertEquals(8, movies.get(0).getCriticScore());
            });
    }

    @Test
    @WithMockUser
    void moviesByCriticScoreFailTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .criticScore(3)
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.findById(Long.valueOf(4)))
            .thenReturn(Optional.of(wishlist));

        String query =
            """
            query {
              moviesByCriticScore(id: 4, criticScore: 8) {
                id,
                title,
                criticScore
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("moviesByCriticScore")
            .entityList(Movie.class)
            .hasSize(1)
            .satisfies(movies -> {
                assertNotEquals(2, movies.get(0).getCriticScore());
            });
    }

    @Test
    @WithMockUser
    void ratingsTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .criticScore(3)
            .build();
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();

        Rating ratingOne = Rating
            .builder()
            .id(Long.valueOf(1))
            .profile(profile)
            .rating(Double.valueOf(8))
            .review("Excellent")
            .movie(movieOne)
            .build();

        Rating ratingTwo = Rating
            .builder()
            .id(Long.valueOf(2))
            .profile(profile)
            .rating(Double.valueOf(3))
            .review("I hated it")
            .movie(movieTwo)
            .build();

        Mockito
            .when(ratingService.getAllForClient(client.getId()))
            .thenReturn(List.of(ratingOne, ratingTwo));

        String query =
            """
                query{
                  ratings{
                    review,
                    rating,
                    profile{
                      name
                    },
                    movie{
                      title,
                      criticScore
                    }
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("ratings")
            .entityList(Rating.class)
            .hasSize(2);
    }

    @Test
    @WithMockUser
    void ratingsFailTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .criticScore(3)
            .build();
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();

        Rating ratingOne = Rating
            .builder()
            .id(Long.valueOf(1))
            .profile(profile)
            .rating(Double.valueOf(8))
            .review("Excellent")
            .movie(movieOne)
            .build();

        Rating ratingTwo = Rating
            .builder()
            .id(Long.valueOf(2))
            .profile(profile)
            .rating(Double.valueOf(3))
            .review("I hated it")
            .movie(movieTwo)
            .build();

        Mockito
            .when(ratingService.getAllForClient(client.getId()))
            .thenReturn(List.of(ratingOne, ratingTwo));

        String query =
            """
                query{
                  ratings{
                    review,
                    rating,
                    profile{
                      name
                    },
                    movie{
                      title,
                      criticScore
                    }
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("ratings")
            .entityList(Rating.class)
            .satisfies(ratings -> {
                assertFalse(ratings.size() == 8);
            });
    }

    @Test
    @WithMockUser
    void ratingByIdTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .criticScore(3)
            .build();
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();

        Rating ratingOne = Rating
            .builder()
            .id(Long.valueOf(1))
            .profile(profile)
            .rating(Double.valueOf(8))
            .review("Excellent")
            .movie(movieOne)
            .build();

        Rating ratingTwo = Rating
            .builder()
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

        String query =
            """
            query {
              ratingById(id: 2) {
                id,
                review,
                rating
              }
            }
            """;

        graphQlTester
            .document(query)
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
    void ratingByIdFailTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .criticScore(3)
            .build();
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();

        Rating ratingOne = Rating
            .builder()
            .id(Long.valueOf(1))
            .profile(profile)
            .rating(Double.valueOf(8))
            .review("Excellent")
            .movie(movieOne)
            .build();

        Rating ratingTwo = Rating
            .builder()
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

        String query =
            """
            query {
              ratingById(id: 2) {
                id,
                review,
                rating
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("ratingById")
            .entity(Rating.class)
            .satisfies(rating -> {
                assertNotEquals(23, rating.getId());
            });
    }

    @Test
    @WithMockUser
    void ratingsByProfileTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .criticScore(3)
            .build();
        Profile profileOne = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Profile profileTwo = Profile
            .builder()
            .id(Long.valueOf(5))
            .name("profile5")
            .client(client)
            .build();

        Rating ratingOne = Rating
            .builder()
            .id(Long.valueOf(1))
            .profile(profileOne)
            .rating(Double.valueOf(8))
            .review("Excellent")
            .movie(movieOne)
            .build();

        Rating ratingTwo = Rating
            .builder()
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

        String query =
            """
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

        graphQlTester
            .document(query)
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
    void ratingsByProfileFailTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .criticScore(3)
            .build();
        Profile profileOne = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Profile profileTwo = Profile
            .builder()
            .id(Long.valueOf(5))
            .name("profile5")
            .client(client)
            .build();

        Rating ratingOne = Rating
            .builder()
            .id(Long.valueOf(1))
            .profile(profileOne)
            .rating(Double.valueOf(8))
            .review("Excellent")
            .movie(movieOne)
            .build();

        Rating ratingTwo = Rating
            .builder()
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

        String query =
            """
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

        graphQlTester
            .document(query)
            .execute()
            .path("ratingsByProfile")
            .entityList(Rating.class)
            .hasSize(1)
            .satisfies(ratings -> {
                assertFalse(ratings.size() == 3);
            });
    }

    @Test
    @WithMockUser
    void ratingsByMovieTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(10))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(15))
            .title("Test2")
            .criticScore(3)
            .build();

        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();

        Rating ratingOne = Rating
            .builder()
            .id(Long.valueOf(1))
            .profile(profile)
            .rating(Double.valueOf(8))
            .review("Excellent")
            .movie(movieOne)
            .build();

        Rating ratingTwo = Rating
            .builder()
            .id(Long.valueOf(2))
            .profile(profile)
            .rating(Double.valueOf(3))
            .review("I hated it")
            .movie(movieOne)
            .build();

        Rating ratingThree = Rating
            .builder()
            .id(Long.valueOf(4))
            .profile(profile)
            .rating(Double.valueOf(5))
            .review("I would not see it again")
            .movie(movieOne)
            .build();

        Rating ratingOtherMovie = Rating
            .builder()
            .id(Long.valueOf(3))
            .profile(profile)
            .rating(Double.valueOf(5))
            .review("meh")
            .movie(movieTwo)
            .build();

        Mockito
            .when(
                ratingService.getByMovieIdForClient(
                    movieOne.getId(),
                    client.getId()
                )
            )
            .thenReturn(List.of(ratingOne, ratingTwo, ratingThree));

        Mockito
            .when(
                ratingService.getByMovieIdForClient(
                    movieTwo.getId(),
                    client.getId()
                )
            )
            .thenReturn(List.of(ratingOtherMovie));

        String query =
            """
            query {
              ratingsByMovie(movieId: \"10\") {
                review,
                rating,
                id,
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("ratingsByMovie")
            .entityList(Rating.class)
            .hasSize(3);
    }

    @Test
    @WithMockUser
    void ratingsByMovieFailTest() {
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(10))
            .title("Test")
            .criticScore(8)
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(15))
            .title("Test2")
            .criticScore(3)
            .build();

        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();

        Rating ratingOne = Rating
            .builder()
            .id(Long.valueOf(1))
            .profile(profile)
            .rating(Double.valueOf(8))
            .review("Excellent")
            .movie(movieOne)
            .build();

        Rating ratingTwo = Rating
            .builder()
            .id(Long.valueOf(2))
            .profile(profile)
            .rating(Double.valueOf(3))
            .review("I hated it")
            .movie(movieOne)
            .build();

        Rating ratingThree = Rating
            .builder()
            .id(Long.valueOf(4))
            .profile(profile)
            .rating(Double.valueOf(5))
            .review("I would not see it again")
            .movie(movieOne)
            .build();

        Rating ratingOtherMovie = Rating
            .builder()
            .id(Long.valueOf(3))
            .profile(profile)
            .rating(Double.valueOf(5))
            .review("meh")
            .movie(movieTwo)
            .build();

        Mockito
            .when(
                ratingService.getByMovieIdForClient(
                    movieOne.getId(),
                    client.getId()
                )
            )
            .thenReturn(List.of(ratingOne, ratingTwo, ratingThree));

        Mockito
            .when(
                ratingService.getByMovieIdForClient(
                    movieTwo.getId(),
                    client.getId()
                )
            )
            .thenReturn(List.of(ratingOtherMovie));

        String query =
            """
            query {
              ratingsByMovie(movieId: \"10\") {
                review,
                rating,
                id,
              }
            }
            """;

        graphQlTester
            .document(query)
            .execute()
            .path("ratingsByMovie")
            .entityList(Rating.class)
            .satisfies(ratings -> {
                assertFalse(ratings.size() > 10);
            });
    }

    // TITLE DETAIL TESTS
    @Test
    @WithMockUser
    void titleDetailTest() {
        WatchModeSource src = new WatchModeSource();
        src.setName("Hulu");
        src.setType("sub");

        List<WatchModeSource> sources = new ArrayList<>();
        sources.add(src);

        List<Long> genres = new ArrayList<>();
        genres.add(666L);

        List<String> genreNames = new ArrayList<>();
        genreNames.add("Horror");

        List<Long> networks = new ArrayList<>();
        networks.add(3L);

        List<String> networkNames = new ArrayList<>();
        networkNames.add("HBO");

        TitleDetail td = new TitleDetail();
        td.setTitle("Movie 1");
        td.setId(1L);
        td.setOriginalTitle("Movie 1");
        td.setPlotOverview("Plot");
        td.setType("Type");
        td.setRuntimeMinutes(120);
        td.setYear(2022);
        td.setEndYear(null);
        td.setReleaseDate("1-1-1");
        td.setImdbId("1");
        td.setTmdbId(1L);
        td.setGenres(genres);
        td.setGenreNames(genreNames);
        td.setUserRating(5.0);
        td.setPoster("Poster");
        td.setBackdrop("Backdrop");
        td.setOriginalLanguage("English");
        td.setNetworks(networks);
        td.setNetworkNames(networkNames);
        td.setTrailer("Trailer");
        td.setTrailerThumbnail("Thumb");
        td.setRelevancePercentile(0.9);
        td.setSources(sources);

        Mockito
            .when(watchModeService.getTitleDetail("1616666", true))
            .thenReturn(td);

        String query =
            """
                query {
                  titleDetail(id : 1616666) {
                    title
                    id
                    originalTitle
                    plotOverview
                    type
                    runtimeMinutes
                    year
                    endYear
                    releaseDate
                    imdbId
                    tmdbId
                    genres
                    genreNames
                    userRating
                    criticScore
                    usRating
                    poster
                    backdrop
                    originalLanguage
                    networks
                    networkNames
                    trailer
                    trailerThumbnail
                    relevancePercentile
                    sources {
                      name
                      type
                    }
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("titleDetail")
            .entity(TitleDetail.class)
            .satisfies(titleDetail -> {
                assertEquals(td.getTitle(), titleDetail.getTitle());
                assertEquals(td.getSources().get(0).getName(), src.getName());
                assertEquals(td.getSources().get(0).getType(), src.getType());
                assertEquals(td, titleDetail);
            });
    }

    // WISHLIST TESTS
    @Test
    @WithMockUser
    void wishlistsTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Movie movieOne = Movie
            .builder()
            .id(Long.valueOf(137939))
            .title("Test")
            .genreString("comedy")
            .build();
        Movie movieTwo = Movie
            .builder()
            .id(Long.valueOf(1939))
            .title("Test2")
            .genreString("drama")
            .build();

        Wishlist wishlist = Wishlist
            .builder()
            .id(Long.valueOf(4))
            .name("wishlist of profile3")
            .profile(profile)
            .movies(List.of(movieOne, movieTwo))
            .build();

        Mockito
            .when(wishlistService.getAllForClient(Long.valueOf(1)))
            .thenReturn(List.of(wishlist));

        String query =
            """
                query {
                  wishlists {
                    id
                    name
                    movies {
                      details {
                        title
                      }
                    }
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("wishlists")
            .entityList(Wishlist.class)
            .hasSize(1)
            .satisfies(wishlists -> {
                assertEquals(
                    "wishlist of profile3",
                    wishlists.get(0).getName()
                );
            });
    }

    @Test
    @WithMockUser
    void networksTest() {
        WatchModeNetwork hbo = new WatchModeNetwork(1, "HBO", "USA", 1);
        List<WatchModeNetwork> networks = List.of(hbo);

        Mockito.when(watchModeService.getAllNetworks()).thenReturn(networks);

        String query =
            """
                query {
                  networks {
                    id
                    name
                    origin_country
                    tmdb_id
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("networks")
            .entityList(WatchModeNetwork.class)
            .hasSize(1)
            .satisfies(retNetworks -> {
                assertEquals(networks, retNetworks);
            });
    }

    @Test
    @WithMockUser
    void searchTitlesTest() {
        // WatchModeNetwork hbo = new WatchModeNetwork(1, "HBO", "USA", 1);
        // List<WatchModeNetwork> networks = List.of(hbo);

        TitleSearchResult tsr = new TitleSearchResult();
        tsr.setName("Movie");
        tsr.setId(1L);
        tsr.setRelevance(10.0);

        List<TitleSearchResult> results = List.of(tsr);

        TitleSearchResponse tsResponse = new TitleSearchResponse(results);

        Mockito
            .when(watchModeService.getTitlesBySearch("Movie"))
            .thenReturn(tsResponse);

        String query =
            """
                query {
                  searchTitles (title : "Movie") {
                    id
                    name
                    relevance
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("searchTitles")
            .entityList(TitleSearchResult.class)
            .hasSize(1)
            .satisfies(retResults -> {
                assertEquals(results, retResults);
            });
    }
}
