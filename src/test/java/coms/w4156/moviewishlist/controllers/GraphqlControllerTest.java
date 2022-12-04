package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
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
    void setUp() {
      client = Client.builder().id(Long.valueOf("1")).email("user").build();
      Mockito
          .when(clientService.findByEmail("client"))
          .thenReturn(Optional.of(client));
    }
    @WithMockUser
    @Test
    void clientsTest() {
        String query = """
                query{
                    clients{
                        id
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("clients")
                .entityList(Client.class)
                .hasSize(1);
    }

    @Test
    void clientByIdTest() {
        String query = """
                query clientById($id: ID!){
                  clientById(id: $id){
                    email,
                    profiles{
                      id,
                      wishlists{
                        id,
                        movies{
                          movieName,
                          movieRuntime
                        }
                      }
                    }
                  }
                }
                """;
        graphQlTester.document(query)
                .variable("id", 1)
                .execute()
                .path("clientById")
                .entity(Client.class)
                .satisfies(client -> {
                    assertEquals("testGrahphQL2@test.com",client.getEmail());
                    assertEquals("1", client.getId());
                });
    }

    @WithMockUser
    @Test
    void profilesTest() {
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
                    assertEquals("2", profiles.get(0).getId());
                });
    }

    @Test
    void profileByUDTest(){
        String query = """
                query profileByID($id: ID!){
                  profileByID(id: $id){
                    id
                  }
                }
                """;

        graphQlTester.document(query)
                .variable("id", 1)
                .execute()
                .path("profileByID")
                .entity(Profile.class)
                .satisfies(profile -> {
                    assertEquals(1, profile.getId());
                });
    }


    @Test
    void moviesTest(){
        String query = """
                query {
                  movies{
                    id,
                    wishlists{
                      name
                    },
                    movieName,
                    movieRuntime,
                    movieReleaseYear
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("movies")
                .entityList(Movie.class)
                .hasSize(2)
                .satisfies(movies -> {
                    //TODO:
                });
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
}