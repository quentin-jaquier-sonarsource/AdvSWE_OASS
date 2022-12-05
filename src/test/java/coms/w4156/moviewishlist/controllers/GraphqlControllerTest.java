package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResult;
import coms.w4156.moviewishlist.repositories.ClientRepository;
import coms.w4156.moviewishlist.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
    void profileByUDTest(){
        Profile profile = Profile.builder().id(Long.valueOf(6)).name("profile6").client(client).build();
        Mockito
                .when(profileService.findById(Long.valueOf(6)))
                .thenReturn(Optional.ofNullable(profile));

        String query = """
                query profileByID($id: ID!){
                  profileByID(id: $id){
                    id
                  }
                }
                """;

        graphQlTester.document(query)
                .variable("id", 6)
                .execute()
                .path("profileByID")
                .entity(Profile.class)
                .satisfies(p -> {
                    assertEquals(6, p.getId());
                });
    }


    @Test
    void moviesTest(){
        Movie m = Movie.builder().id(Long.valueOf(137939)).movie_name("Test").movie_gener("comedy").build();
        Movie m2 = Movie.builder().id(Long.valueOf(1939)).movie_name("Test2").movie_gener("drama").build();
        Mockito
                .when(movieService.getAll())
                .thenReturn(List.of(m));

        String query = """
                query {
                  movies{
                    id,
                    movieName
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
    void moviesByGenreTest(){
        String query = """
                query moviesByGenre($wishlistID: ID!,
                 $genre: String){
                  moviesByGenre(wishlistID: $id,
                    genre: $genre){
                    id,
                    movieName,
                    movieRuntime
                  }
                }
                """;
        graphQlTester.document(query)
                .variable("id", 4)
                .variable("genre", "drama")
                .execute()
                .path("moviesByGenre")
                .entityList(Movie.class)
                .hasSize(1)
                .satisfies(movies -> {
                    assertEquals("drama", movies.get(0).getMovie_gener());
                });
    }

    @Test
    void moviesByReleaseYearTest() {
        String query = """
                query moviesByReleaseYear($wishlistID: ID!,
                 $movieReleaseYear: String){
                  moviesByGenre(wishlistID: $id,
                    movieReleaseYear: $year){
                    id,
                    movieName,
                    movieRuntime
                  }
                }
                """;
        graphQlTester.document(query)
                .variable("id", 4)
                .variable("year", "2004")
                .execute()
                .path("moviesByReleaseYear")
                .entityList(Movie.class)
                .hasSize(1)
                .satisfies(movies -> {
                    assertEquals("2004", movies.get(0).getMovie_release_year());
                });
    }

    @Test
    void moviesByRuntimeTest() {
        String query = """
                query moviesByReleaseYear($wishlistID: ID!,
                 $runtime: Int){
                  moviesByRuntime(wishlistID: $id,
                    runtime: $runtime){
                    id,
                    movieName,
                    movieRuntime
                  }
                }
                """;
        graphQlTester.document(query)
                .variable("id", 4)
                .variable("runtime", "53")
                .execute()
                .path("moviesByRuntime")
                .entityList(Movie.class)
                .hasSize(1)
                .satisfies(movies -> {
                    assertEquals("53", movies.get(0).getMovie_runtime());
                });
    }

    @Test
    void moviesByCriticScoreTest() {
        String query = """
                query moviesByReleaseYear($wishlistID: ID!,
                 $criticScore: Int){
                  moviesByGenre(wishlistID: $id,
                    criticScore: $score){
                    id,
                    movieName,
                    movieRuntime
                  }
                }
                """;
        graphQlTester.document(query)
                .variable("id", 4)
                .variable("score", "110")
                .execute()
                .path("moviesByCriticScore")
                .entityList(Movie.class)
                .hasSize(1)
                .satisfies(movies -> {
                    assertEquals("53", movies.get(0).getCritic_score());
                });

    }

    @Test
    void searchTitlesTest() {
        String query = """
                query searchTitles($title: String!){
                  searchTitles(title: $title){
                    id,
                    details{
                      criticScore
                    }
                  }
                }
                """;
        graphQlTester.document(query)
                .variable("title", "Me Before You")
                .execute()
                .path("searchTitles")
                .entityList(TitleSearchResult.class)
                .satisfies(titleSearchResults -> {
                    assertEquals(2016, titleSearchResults.get(0).getYear());
                });

    }

//    @Test
//    void networksTest() {
//        String query = """
//                """;
//        graphQlTester.document()
//                .execute()
//                .path()
//    }

    @Test
    void titleDetailTest() {
        String query = """
                query titleDetail($id: ID!){
                  titleDetail(id: $id){
                    id,
                    originalTitle,
                    releaseDate,
                    usRating,
                    genreNames
                  }
                }
                """;
        graphQlTester.document(query)
                .variable("id", 1409931)
                .execute()
                .path("titleDetail")
                .entity(Movie.class)
                .satisfies(movie -> {
                    assertEquals("The Notebook", movie.getMovie_name());
                });
    }

    @Test
    void ratingsTest(){
        String query = """
                query{
                  ratings{
                    review,
                    rating,
                    profile{
                      name
                    },
                    movie{
                      movieName,
                      movieCriticScore
                    }
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("ratings")
                .entityList(Rating.class)
                .satisfies(ratings -> {
                    //TODO
                });
    }

    @Test
    void ratingsByIdTest(){
        String query = """
                query ratingsById($id:ID!){
                  ratingsById(id: $id){
                    review,
                    rating
                  }
                }
                """;

        graphQlTester.document(query)
                .variable("id", 2)
                .execute()
                .path("ratingsById")
                .entity(Rating.class)
                .satisfies(rating -> {
                    assertEquals(1,rating.getId());
                });
    }

    @Test
    void ratingsByProfileTest(){
        String query = """
                query ratingsByProfile($profileId : String!){
                  ratingsByProfile(profileId: $id){
                    review,
                    rating,
                    profile{
                      id
                    }
                  }
                }
                """;

        graphQlTester.document(query)
                .variable("id", 23)
                .execute()
                .path("ratingsByProfile")
                .entityList(Rating.class)
                .satisfies(ratings -> {
                    assertEquals(23, ratings.get(0).getProfileId());
                });
    }

    @Test
    void ratingsByMovieTest(){
        String query = """
                query ratingsByMovie($movieId : String!){
                  ratingsByMovie(movieId: $id)
                  {
                    review,
                    rating
                  }
                }
                """;

        graphQlTester.document(query)
                .variable("id", "1250035")
                .execute()
                .path("ratingsByMovie")
                .entityList(Rating.class)
                .satisfies(ratings -> {
                    assertEquals(1250035, ratings.get(0).getMovieId());
                });
    }
}