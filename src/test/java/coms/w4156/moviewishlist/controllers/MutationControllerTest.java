package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.junit.jupiter.api.Assertions.*;

@Import({ClientService.class,
        MovieService.class,
        WatchModeService.class,
        WishlistService.class,
        ProfileService.class})
@GraphQlTest(GraphqlController.class)
class MutationControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Test
    void createClient() {
    }

    @Test
    void updateClientTest() {
    }

    @Test
    void deleteClientTest() {
    }

}