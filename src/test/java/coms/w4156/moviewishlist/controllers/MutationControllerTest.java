package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.*;

import static org.junit.jupiter.api.Assertions.*;

//@GraphQlTest(GraphqlController.class)
@SpringBootTest
@AutoConfigureHttpGraphQlTester
@AutoConfigureWebTestClient
@AutoConfigureGraphQlTester
class MutationControllerTest {

//    @Autowired
//    GraphQlTester graphQlTester;

    @Autowired
    @MockBean
    private WebGraphQlTester graphQlTester;

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

    WebGraphQlTester tester = this.graphQlTester.mutate()
            .headers(headers -> headers.setBearerAuth("Bearer "))
            .build();

    @Test
    void createClientTest() {
        String query = """
                mutation createClient($email: String!){
                  createClient(
                    email: $email){
                    email
                  } 
                }
                """;

        tester.document(query)
                .variable("email", "test92@test.com")
                .execute()
                .path("createClient")
                .entity(Client.class)
                .satisfies(client -> {
                    assertEquals("test92@test.com", client.getEmail());
                   // assertEquals("test90@test.com", client.getProfiles().get(0).getClient().getEmail());
                });

    }

    @Test
    void updateClientTest() {
    }

    @Test
    void deleteClientTest() {
    }

    @Test
    void createProfileTest() {
    }

    @Test
    void updateProfileTest() {
    }

    @Test
    void deleteProfileTest() {
    }

    @Test
    void createWishlistTest() {
    }

    @Test
    void updateWishlistTest() {
    }

    @Test
    void deleteWishlistTest() {
    }

    @Test
    void addMovieToWishlistTest() {
    }
}