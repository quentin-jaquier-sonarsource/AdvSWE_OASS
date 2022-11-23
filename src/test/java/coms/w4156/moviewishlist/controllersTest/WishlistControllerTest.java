package coms.w4156.moviewishlist.controllersTest;

import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.WishlistService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import coms.w4156.moviewishlist.services.ProfileService;
import coms.w4156.moviewishlist.controllers.WishlistController;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

@RunWith(MockitoJUnitRunner.Silent.class)
public class WishlistControllerTest {

    /**
     * Mocks MVC for testing endpoints.
     */
    private MockMvc mockMvc;


    /**
     * Mocks the wishlist service.
     */
    @Mock
    private WishlistService wishlistService;

    /**
     * Mocks the profile service.
     */
    @MockBean
    private ProfileService profileService;

    /**
     * Injects the wishlist controller with the mocked services.
     */
    @InjectMocks
    private WishlistController wishlistController;


    /**
     * Mock data for Testing.
     */
    private Profile profile = Profile.builder()
        .name("omniyyah")
        .build();

    /**
     * Mock data for Testing.
     */
    private Profile profile3 = Profile.builder()
        .name("omniyyah")
        .build();

    /**
     * Mock data for Testing.
     */
    private Wishlist wishlist1 = Wishlist.builder()
        .name("wishlist1 for omniyyah")
        .profile(profile)
        .build();

    /**
     * Setup for testing.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(wishlistController)
            .build();
    }

    /**
     * Test for creating a wishlist.
     * @throws Exception if there is an error.
     */
    @Test
    public void shouldSuccessfullyCreateAWishlist() throws Exception {
        Wishlist wishlist = Wishlist.builder()
            .name("test wishlist")
            .profile(profile)
            .build();

        Mockito
            .when(wishlistService.create(wishlist))
            .thenReturn(wishlist);

        final String content =
            String.format(
                "{\"name\":\"%s\",\"profile\":{\"name\":\"%s\"},\"movies\":[]}",
                "test wishlist",
                profile.getName()
            );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/wishlists")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isOk());
            // .andExpect(jsonPath("$", notNullValue()))
            // .andExpect(jsonPath("$.name", is("test wishlist")));
    }

    /**
     * Test for creating a wishlist with an invalid profile.
     * @throws Exception if there is an error.
     */
    @Test
    public void shouldFailTryingToCreateAnInvalidProfile() throws Exception {
        final Wishlist wishlist = Wishlist.builder()
            .name("test wishlist")
            .profile(profile3)
            .build();

        Mockito
            .when(wishlistService.create(wishlist))
            .thenReturn(wishlist);

        final String content =
            String.format(
                "{\"name\":\"\",\"profile\":{\"name\":\"%s\"},\"movies\":[]}",
                profile3.getName()
            );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/wishlists")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());

    }

    /**
     * Test for Updating a wishlist.
     * @throws Exception if there is an error.
     */
    @Test
    public void shouldUpdateWishlist() throws Exception {

        final Wishlist updatedWishlist = Wishlist.builder()
            .name("updated test wishlist")
            .profile(profile)
            .build();

        Mockito
            .when(wishlistService.findById(updatedWishlist.getId()))
            .thenReturn(java.util.Optional.of(updatedWishlist));

        Mockito
            .when(wishlistService.update(updatedWishlist))
            .thenReturn(updatedWishlist);

        final String content =
            String.format(
                "{\"name\":\"%s\",\"profile\":{\"name\":\"%s\"},\"movies\":[]}",
                "updated test wishlist",
                profile3.getName()
            );

        final long wishlistId = 9L;

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(String.format("/wishlists/%d", wishlistId))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isNoContent());
    }

    /**
     * Test for fetching a wishlist by ID.
     * @throws Exception if there is an error.
     */
    @Test
    public void getWishlistById() throws Exception {
        Mockito
            .when(wishlistService.findById(wishlist1.getId()))
            .thenReturn(java.util.Optional.of(wishlist1));

    }

    /**
     * Test for deleting a wishlist.
     * @throws Exception if there is an error.
     */
    @Test
    public void deleteWishlist() throws Exception {
        final String content =
            String.format(
                "{\"name\":\"%s\",\"profile\":{\"name\":\"%s\"},\"movies\":[]}",
                "delete test wishlist",
                profile.getName()
            );

        final Long wishlistId = 9L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete(String.format("/wishlists/%d", wishlistId))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isNoContent());

    }

    /**
     * Test for deleting all wishlists.
     * @throws Exception if there is an error.
     */
    @Test
    public void deleteAllWishlists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/wishlists")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
