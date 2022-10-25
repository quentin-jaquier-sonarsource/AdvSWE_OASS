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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import coms.w4156.moviewishlist.services.UserService;
import coms.w4156.moviewishlist.controllers.WishlistController;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WishlistControllerTest {

    private MockMvc mockMvc;

    //we need to convert from json to string and vice versa
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    //Mock wishlist repo
    @Mock
    private WishlistService wishlistService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private WishlistController wishlistController;


    User user = User.builder()
        .email("omniyyah@gmail.com")
        .name("omniyyah")
        .password("hjgT48582%%")
        .build();

    User user2 = User.builder()
        .email("kate@gmail.com")
        .name("kate")
        .password("fjjfi22")
        .build();

    User user3 = User.builder()
        .email("userTwo")
        .name("omniyyah")
        .password("hjgT48582%%")
        .build();

    Wishlist wishlist1 = Wishlist.builder()
        .name("wishlist1 for omniyyah")
        .user(user)
        .build();


    Wishlist wishlist2 = Wishlist.builder()
        .name("wishlist1 for 2")
        .user(user2)
        .build();

    Wishlist wishlist3 = Wishlist.builder()
        .name("wishlist1 for 3")
        .user(user2)
        .build();

    Wishlist wishlist4 = Wishlist.builder()
        .name("")
        .user(user)
        .build();

    Wishlist wishlist5 = Wishlist.builder()
        .name("")
        .user(user3)
        .build();


    //should pass
//    Wishlist wishlist1 = new Wishlist("wishlist1 for omniyyah", user);
//    Wishlist wishlist2 = new Wishlist("wishlist1 for 2", user2);
//    Wishlist wishlist3 = new Wishlist("wishlist2 for 2", user2);
//
//    //should not pass
//    Wishlist wishlist4 = new Wishlist("", user);
//    Wishlist wishlist5 = new Wishlist("wishlist1 for 3", user3);


    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(wishlistController).build();
    }

    @Test
    public void createWishlist_success() throws Exception{
        Wishlist wishlist = Wishlist.builder()
                .name("test wishlist")
                .user(user)
                .build();

        Mockito.when(wishlistService.create(wishlist)).thenReturn(wishlist);
        String content = "{\"name\":\"test wishlist\", \"user\": {\"email\":\"" + user.getEmail() + "\"}, \"movies\": []}";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/wishlists")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isOk());
                // .andExpect(jsonPath("$", notNullValue()))
                // .andExpect(jsonPath("$.name", is("test wishlist")));

    }

    @Test
    public void createWishlist_fail() throws Exception{
        Wishlist wishlist = Wishlist.builder()
                .name("test wishlist")
                .user(user3)
                .build();

        Mockito.when(wishlistService.create(wishlist)).thenReturn(wishlist);
        String content = objectWriter.writeValueAsString(wishlist);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/wishlists")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

    }


    @Test
    public void updateWishlist() throws Exception{

        Wishlist updatedWishlist = Wishlist.builder()
                .name("updated test wishlist")
                .user(user)
                .build();

        Mockito.when(wishlistService.findById(updatedWishlist.getId())).thenReturn(java.util.Optional.of(updatedWishlist));
        Mockito.when(wishlistService.update(updatedWishlist)).thenReturn(updatedWishlist);

        String content = objectWriter.writeValueAsString(updatedWishlist);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/wishlists")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("updated test wishlist")));

    }


    @Test
    public void getAllWishlists() throws Exception{
        List<Wishlist> wishlists = new ArrayList<>(Arrays.asList(wishlist1,
                wishlist2, wishlist3, wishlist4, wishlist5));

        Mockito.when(wishlistService.getAll()).thenReturn(wishlists);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/wishlists")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name", is("wishlist1 for 2")));

    }

    @Test
    public void getWishlistById() throws Exception{
        Mockito.when(wishlistService.findById(wishlist1.getId()))
                .thenReturn(java.util.Optional.of(wishlist1));

    }


    @Test
    public void deleteWishlist() throws Exception{
        Wishlist deletedWishlist = Wishlist.builder()
                .name("deleted test wishlist")
                .user(user)
                .build();

        Mockito.when(wishlistService.findById(deletedWishlist.getId())).thenReturn(java.util.Optional.of(deletedWishlist));
        Mockito.when(wishlistService.update(deletedWishlist)).thenReturn(deletedWishlist);

        String content = objectWriter.writeValueAsString(deletedWishlist);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/wishlists")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isOk());


    }

    @Test
    public void deleteAllWishlists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/wishlists")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
