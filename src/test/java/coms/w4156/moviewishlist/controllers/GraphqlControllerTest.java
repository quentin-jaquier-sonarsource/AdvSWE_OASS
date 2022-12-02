package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.services.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.junit.jupiter.api.Assertions.*;

/* Integration Test */

@Import({ClientService.class,
        MovieService.class,
        WatchModeService.class,
        WishlistService.class,
        ProfileService.class})
@GraphQlTest(GraphqlController.class)
class GraphqlControllerTest {

    @Autowired
    GraphQlTester graphQlTester;

    @Test
    void getAllClientsTest(){

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
                .entityList(Client.class);
    }

    @Test
    void clientByIdTest(){
        String query = """
                query clientById($id: ID){
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

    @Test
    void profilesTest(){
        String query = """
                query {
                  profiles{
                    client{
                      id
                    }
                    id
                  }
                }
                """;
        graphQlTester.document(query)
                .execute()
                .path("profiles")
                .entityList(Profile.class)
                .satisfies(profiles -> {
                    assertEquals("1", profiles.get(0).getClient().getId());
                    assertEquals("2", profiles.get(0).getId());
                });
    }

}