package coms.w4156.moviewishlist.controllersTest;

import coms.w4156.moviewishlist.models.User;
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
import coms.w4156.moviewishlist.services.UserService;
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
     * Mocks the user service.
     */
    @MockBean
    private UserService userService;

    /**
     * Injects the wishlist controller with the mocked services.
     */
    @InjectMocks
    private WishlistController wishlistController;


    /**
     * Mock data for Testing.
     */
    private User user = User.builder()
        .email("omniyyah@gmail.com")
        .name("omniyyah")
        .password("hjgT48582%%")
        .build();

    /**
     * Mock data for Testing.
     */
    private User user3 = User.builder()
        .email("userTwo")
        .name("omniyyah")
        .password("hjgT48582%%")
        .build();

    /**
     * Mock data for Testing.
     */
    private Wishlist wishlist1 = Wishlist.builder()
        .name("wishlist1 for omniyyah")
        .user(user)
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
            .user(user)
            .build();

        Mockito
            .when(wishlistService.create(wishlist))
            .thenReturn(wishlist);

        final String content =
            String.format(
                "{\"name\":\"%s\",\"user\":{\"email\":\"%s\"},\"movies\":[]}",
                "test wishlist",
                user.getEmail()
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
     * Test for creating a wishlist with an invalid user.
     * @throws Exception if there is an error.
     */
    @Test
    public void shouldFailTryingToCreateAnInvalidUser() throws Exception {
        final Wishlist wishlist = Wishlist.builder()
            .name("test wishlist")
            .user(user3)
            .build();

        Mockito
            .when(wishlistService.create(wishlist))
            .thenReturn(wishlist);

        final String content =
            String.format(
                "{\"name\":\"\",\"user\":{\"email\":\"%s\"},\"movies\":[]}",
                user3.getEmail()
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
            .user(user)
            .build();

        Mockito
            .when(wishlistService.findById(updatedWishlist.getId()))
            .thenReturn(java.util.Optional.of(updatedWishlist));

        Mockito
            .when(wishlistService.update(updatedWishlist))
            .thenReturn(updatedWishlist);

        final String content =
            String.format(
                "{\"name\":\"%s\",\"user\":{\"email\":\"%s\"},\"movies\":[]}",
                "updated test wishlist",
                user3.getEmail()
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
                "{\"name\":\"%s\",\"user\":{\"email\":\"%s\"},\"movies\":[]}",
                "delete test wishlist",
                user.getEmail()
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
