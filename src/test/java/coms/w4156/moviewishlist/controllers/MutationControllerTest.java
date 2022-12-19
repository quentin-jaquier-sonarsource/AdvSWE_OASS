package coms.w4156.moviewishlist.controllers;


import coms.w4156.moviewishlist.models.*;
import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.RatingService;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.services.WishlistService;

import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doReturn;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MutationControllerTest {

    private static final String GRAPHQL_PATH = "/graphql";

    @Autowired
    private WebTestClient webTestClient;

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
                .when(clientService.findByEmail("anonymousUser"))
                .thenReturn(Optional.of(client));
    }



    @Test
    @WithMockUser
    void createClientTest() {

        Client tempClient = new Client("user2", new HashSet<Role>());
        Mockito.when(clientService.create(tempClient)).thenReturn(tempClient);

        String q = """
                mutation {
                   createClient(email: "user2") {
                     id
                   }
                 }
                """;

        webTestClient.post()
                .uri(GRAPHQL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(toJSON(q)), String.class)
                .exchange()
                .expectStatus().isOk();


    }

    @Test
    @WithMockUser
    void updateClientTest() {

    Client ct = client;
    ct.setEmail("user2");

    Mockito.when(clientService.findById(1L)).thenReturn(Optional.of(ct));

    Mockito
            .when(clientService.update(ct))
            .thenReturn(ct);


    String q = """
            mutation {
              updateClient(id: 1,
              email: "user2"){
                email
              }
            }
            """;

        webTestClient.post()
                .uri(GRAPHQL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(toJSON(q)), String.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser
    void deleteClientTest() {

        Mockito
                .when(clientService.deleteById(1L))
                .thenReturn(Optional.of(client));


        String q = """
                mutation {
                  deleteClient(id: 1){
                    id,
                    email
                  }
                }
                """;

        webTestClient.post()
                .uri(GRAPHQL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(toJSON(q)), String.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithMockUser
    void createProfileTest() {

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);


        Mockito
                .when(profileService.create(profile))
                .thenReturn(profile);

            String q = """
                     mutation {
                      createProfile( clientID: 1, name: "test name"){
                        id,
                        name
                      }
                    }
                    """;


        webTestClient.post()
                .uri(GRAPHQL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(toJSON(q)), String.class)
                .exchange()
                .expectStatus().isOk();


    }




        @Test
        @WithMockUser
        void updateProfileTest() {

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);


            Mockito.when(profileService.findById(1L)).thenReturn(Optional.of(profile));

            Mockito
                    .when(profileService.update(profile))
                    .thenReturn(profile);

            String q = """
                    mutation {
                      updateProfile( id: 1, name: "test name"){
                        id,
                        name
                      }
                    }
                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser
        void deleteProfileTest() {

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);


            Mockito.when(profileService.findById(1L)).thenReturn(Optional.of(profile));

            Mockito
                    .when(profileService.deleteById(1L))
                    .thenReturn(Optional.of(profile));

            String q = """
                    mutation {
                      deleteProfile( id: 1) {
                        id
                      }
                    }

                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser
        void createWishlistTest() {

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);

            Wishlist wl = Wishlist.builder().name("wl1").profile(profile).build();

            Mockito.when(wishlistService.create(wl)).thenReturn(wl);

            Mockito.when(profileService.findById(1L)).thenReturn(Optional.of(profile));


            String q = """
                    mutation {
                     createWishlist(profileID: 1,
                      wishlistName: "wl1"){
                        id,
                        name
                      }
                    }
                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser
        void updateWishlistTest() {

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);

            Wishlist wl = Wishlist.builder().name("wl1").profile(profile).build();

            Mockito.when(wishlistService.findById(1L)).thenReturn(Optional.of(wl));


            Mockito.when(wishlistService.update(wl)).thenReturn(wl);

            String q = """
                    mutation{
                       updateWishlist(id: 1, name: "wl1") {
                         id
                       }
                     }
                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser
        void deleteWishlistTest() {

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);


            Wishlist wl = Wishlist.builder().name("wl1").profile(profile).build();

            Mockito.when(wishlistService.findById(1L)).thenReturn(Optional.of(wl));

            Mockito.when(wishlistService.deleteById(1L)).thenReturn(Optional.ofNullable(wl));

            String q = """
                    mutation {
                      deleteWishlist(id: 1){
                        id,
                        name
                      }
                    }
                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser
        void addMovieToWishlistTest() {

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);

            Movie movie = Movie
                    .builder()
                    .id(Long.valueOf(137939))
                    .title("Test")
                    .criticScore(8)
                    .build();


            Wishlist wl = Wishlist.builder().id(1L).name("wl1").profile(profile).build();

            movie.getWishlists().add(wl);

            Mockito.when(wishlistService.findById(1L)).thenReturn(Optional.of(wl));

            Mockito
                    .when(movieService.findById(137939L))
                    .thenReturn(Optional.ofNullable(movie));

            Mockito.when(wishlistService.update(wl)).thenReturn(wl);

            Mockito
                    .when(movieService.update(movie))
                    .thenReturn(movie);

            String q = """
                    mutation{
                       addMovieToWishlist(
                       wishlistID:1, 
                       movieID:137939) {
                         id
                       }
                     }
                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }


//    @Test
//    @WithMockUser
//    void addMovieToWishlistTest2() {
//
//        Profile profile = new Profile();
//        profile.setName("test name");
//        profile.setClient(client);
//        profile.setId(1L);
//
//        Movie movie = Movie
//                .builder()
//                .id(Long.valueOf(137939))
//                .title("Test")
//                .criticScore(8)
//                .build();
//
//        TitleDetail td = new TitleDetail();
//        td.setTitle("Movie 1");
//        td.setGenres(new ArrayList<>());
//        td.setYear(2012);
//        td.setRuntimeMinutes(120);
//        td.setCriticScore(5);
//
//        Wishlist wl = Wishlist.builder().id(1L).name("wl1").profile(profile).build();
//
//        Movie newMovie = new Movie();
//        newMovie.setId(Long.parseLong("137939"));
//        newMovie.setWishlists(List.of(wl));
//        newMovie.setTitle(td.getTitle());
//        newMovie.setGenres(td.getGenreNames());
//        newMovie.setReleaseYear(td.getYear());
//        newMovie.setRuntimeMinutes(td.getRuntimeMinutes());
//        newMovie.setCriticScore(td.getCriticScore());
//
//
//
//        movie.getWishlists().add(wl);
//
//        Mockito.when(wishlistService.findById(1L)).thenReturn(Optional.of(wl));
//
//        Mockito
//                .when(movieService.findById(137939L))
//                .thenReturn( );
//
//        Mockito
//                .when(watchModeService.getTitleDetail(
//                        "137939",
//                        false
//                ))
//                .thenReturn(td);
//
//        Mockito.when(wishlistService.update(wl)).thenReturn(wl);
//
//        Mockito
//                .when(movieService.update(movie))
//                .thenReturn(movie);
//
//        Mockito
//                .when(movieService.create(newMovie))
//                .thenReturn(newMovie);
//
//        String q = """
//                    mutation{
//                       addMovieToWishlist(
//                       wishlistID:1,
//                       movieID:137939) {
//                         id
//                       }
//                     }
//                    """;
//
//        webTestClient.post()
//                .uri(GRAPHQL_PATH)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(toJSON(q)), String.class)
//                .exchange()
//                .expectStatus().isOk();
//    }


    @Test
        @WithMockUser
        void createRatingTest(){

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);


            Movie movie = Movie
                    .builder()
                    .id(Long.valueOf(137939))
                    .title("Test")
                    .criticScore(8)
                    .build();

            Rating rating = Rating
                    .builder()
                    .profile(profile)
                    .movie(movie)
                    .review("Great Movie")
                    .rating(3.5)
                    .build();



            Mockito
                    .when(profileService.findById(1L))
                            .thenReturn(Optional.of(profile));
            Mockito
                    .when(movieService.findById(137939L))
                    .thenReturn(Optional.ofNullable(movie));

            Mockito
                    .when(ratingService.create(rating))
                    .thenReturn(rating);

            String q = """
                    mutation {
                       createRating(
                       profileId:"1", 
                       movieId: "137939", 
                       review: "Great Movie", 
                       rating : 3.5) {
                         id,
                         rating
                       }
                     }
                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser
        void updateRatingTest(){

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);

            Movie movie = Movie
                    .builder()
                    .id(Long.valueOf(137939))
                    .title("Test")
                    .criticScore(8)
                    .build();

            Rating rating = Rating
                    .builder()
                    .id(1L)
                    .profile(profile)
                    .movie(movie)
                    .review("Great Movie")
                    .rating(3.5)
                    .build();

            rating.setId(1L);

            Mockito
                    .when(profileService.findById(1L))
                    .thenReturn(Optional.of(profile));
            Mockito
                    .when(movieService.findById(137939L))
                    .thenReturn(Optional.ofNullable(movie));

            Mockito
                    .when(ratingService.findById(1L))
                    .thenReturn(Optional.of(rating));

            Mockito
                    .when(ratingService.update(rating))
                    .thenReturn(rating);


            String q = """
                    mutation {
                       updateRating(
                       id: "1", 
                       profileId:"1", 
                       movieId:"137939", 
                       review:"Bad", 
                       rating:2.0) {
                         id,
                         rating
                       }
                     }
                     
                     
                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser
        void deleteRating(){

            Profile profile = new Profile();
            profile.setName("test name");
            profile.setClient(client);
            profile.setId(1L);


            Movie movie = Movie
                    .builder()
                    .id(Long.valueOf(137939))
                    .title("Test")
                    .criticScore(8)
                    .build();

            Rating rating = Rating
                    .builder()
                    .profile(profile)
                    .movie(movie)
                    .review("Great Movie")
                    .rating(3.5)
                    .build();

            rating.setId(1L);

            Mockito
                    .when(profileService.findById(1L))
                    .thenReturn(Optional.of(profile));
            Mockito
                    .when(movieService.findById(137939L))
                    .thenReturn(Optional.ofNullable(movie));

            Mockito
                    .when(ratingService.findById(1L))
                    .thenReturn(Optional.of(rating));

            Mockito
                    .when(ratingService.deleteById(1L))
                    .thenReturn(Optional.of(rating));


            String q = """
                        mutation {
                           deleteRating(id:"1") {
                             id,
                             rating
                           }
                         }
                         
                    """;

            webTestClient.post()
                    .uri(GRAPHQL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(toJSON(q)), String.class)
                    .exchange()
                    .expectStatus().isOk();
        }



    private static String toJSON(String query) {
        try {
            return new JSONObject().put("query", query).toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}

