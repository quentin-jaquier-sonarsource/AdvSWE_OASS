package coms.w4156.moviewishlist.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.MovieService;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.services.RatingService;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.services.WishlistService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.graphql.test.tester.GraphQlTester.Response;
import org.springframework.security.test.context.support.WithMockUser;

@GraphQlTest(GraphqlController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileTests {

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

    @WithMockUser
    @Test
    void profilesTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(3))
            .name("profile3")
            .client(client)
            .build();
        Mockito
            .when(profileService.getAllForClient(client.getId()))
            .thenReturn(List.of(profile));

        String query =
            """
                    query {
                      profiles{
                        id,
                        client {
                          id
                        }
                      }
                    }
                    """;

        graphQlTester
            .document(query)
            .execute()
            .path("profiles")
            .entityList(Profile.class)
            .satisfies(profiles -> {
                assertEquals(1, profiles.get(0).getClient().getId());
                assertEquals(3, profiles.get(0).getId());
            });
    }

    @WithMockUser
    @Test
    void profileByIdTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(6))
            .name("profile6")
            .client(client)
            .build();
        Mockito
            .when(profileService.findById(Long.valueOf(6)))
            .thenReturn(Optional.ofNullable(profile));

        String query =
            """
                query {
                  profileById(id: 6) {
                    id
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("profileById")
            .entity(Profile.class)
            .satisfies(p -> {
                assertEquals(6, p.getId());
            });
    }

    @WithMockUser
    @Test
    void profileByIdFailTest() {
        Profile profile = Profile
            .builder()
            .id(Long.valueOf(6))
            .name("profile6")
            .client(client)
            .build();
        Mockito
            .when(profileService.findById(Long.valueOf(6)))
            .thenReturn(Optional.ofNullable(profile));

        String query =
            """
                query {
                  profileById(id: 6) {
                    id
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .path("profileById")
            .entity(Profile.class)
            .satisfies(p -> {
                assertNotEquals(2, p.getId());
            });
    }

    @WithMockUser
    @Test
    void profileByIdClientNotPresent() {
        // Mock the client not existing
        Mockito
            .when(clientService.findByEmail("user"))
            .thenReturn(Optional.empty());

        String query =
            """
                query {
                  profileById(id: 6) {
                    id
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .errors()
            .expect(err ->
                err.getMessage().equals("ACCESS DENIED: You are not logged in.")
            )
            .verify()
            .path("profileById")
            .valueIsNull();
    }

    @WithMockUser
    @Test
    void profileByIdProfileNotPresent() {
        // Mock the profile not existing
        Mockito.when(profileService.findById(6L)).thenReturn(Optional.empty());

        String query =
            """
                query {
                  profileById(id: 6) {
                    id
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .errors()
            .expect(err ->
                err.getMessage().equals("NOT FOUND: Profile with given id.")
            )
            .verify()
            .path("profileById")
            .valueIsNull();
    }

    @WithMockUser
    @Test
    void profileNotBelongToClient() {
        Wishlist wl = new Wishlist();
        Client c = Client.builder().email("New User").build();
        c.setId(6L);

        // Profile belonging to different client than the one signed in
        Profile profile = new Profile(6L, "MyProfile", List.of(wl), c);

        // Mock the profile existing
        Mockito
            .when(profileService.findById(6L))
            .thenReturn(Optional.ofNullable(profile));

        String query =
            """
                query {
                  profileById(id: 6) {
                    id
                  }
                }
                """;

        graphQlTester
            .document(query)
            .execute()
            .errors()
            .expect(err ->
                err.getMessage().equals("NOT FOUND: Profile with given id.")
            )
            .verify()
            .path("profileById")
            .valueIsNull();
    }
}
