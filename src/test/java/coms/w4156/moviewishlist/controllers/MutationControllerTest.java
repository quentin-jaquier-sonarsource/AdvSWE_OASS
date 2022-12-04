package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.services.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@GraphQlTest(MutationControllerTest.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureHttpGraphQlTester
class MutationControllerTest {

//    @Autowired
//    GraphQlTester graphQlTester;
    @Autowired
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

    Client client;

    String token = "";

//    @BeforeAll
//    void setUp() {
//        client = Client.builder().id(Long.valueOf("1")).email("user").build();
//        Mockito
//                .when(clientService.findByEmail("client"))
//                .thenReturn(Optional.of(client));
//    }

//    @WithMockUser
    @Test
    void createClientTest() {

        WebGraphQlTester tester = this.graphQlTester.mutate()
                .url("http://localhost:8081/graphql")
                .headers(headers -> headers.setBearerAuth(token))
                .build();

        String query = """
                mutation {
                  createClient(
                    email: test92@test.com){
                    email
                  } 
                }
                """;

        tester.document(query)
//                .variable("email", "test92@test.com")
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