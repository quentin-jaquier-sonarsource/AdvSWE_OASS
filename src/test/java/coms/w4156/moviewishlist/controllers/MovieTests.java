package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.RatingService;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.services.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@GraphQlTest(GraphqlController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovieTests {

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
}
