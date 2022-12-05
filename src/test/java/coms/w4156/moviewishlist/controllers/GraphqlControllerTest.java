package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResult;
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
        Profile profile = Profile.builder().id(Long.valueOf(2)).name("profile4").client(client).build();
        Movie movieOne = Movie.builder().id(Long.valueOf(137939)).name("Test").genre("comedy").build();
        Movie movieTwo = Movie.builder().id(Long.valueOf(1939)).name("Test2").genre("drama").build();

        Wishlist wishlist = Wishlist.builder()
            .id(Long.valueOf(4)).name("wishlist of profile4")
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
                    assertEquals("2004", movies.get(0).getReleaseYear());
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
                    assertEquals("53", movies.get(0).getRuntime());
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
                    assertEquals("53", movies.get(0).getCriticScore());
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
                    assertEquals("The Notebook", movie.getName());
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