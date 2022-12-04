package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(GraphqlController.class)
class MutationControllerTest {

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

    @Test
    void createClientTest() {
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