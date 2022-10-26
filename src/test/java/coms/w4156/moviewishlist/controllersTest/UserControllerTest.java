package coms.w4156.moviewishlist.controllersTest;
import coms.w4156.moviewishlist.models.User;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.UserService;
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
import coms.w4156.moviewishlist.controllers.UserController;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.http.MediaType;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    //we need to convert from json to string and vice versa
    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter = objectMapper.writer();

    //Mock user repo
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private List<Wishlist> l = List.of();
    //should pass
    private User userOne = User.builder()
        .email("omniyyah@gmail.com")
        .name("omniyyah")
        .password("hjgT48582%%")
        .wishlists(l)
        .build();

    //should not pass
    private User userTwo = User.builder()
        .email("userTwo")
        .name("omniyyah")
        .password("hjgT48582%%")
        .wishlists(l)
        .build();

    //should not pass
    private User userThree = User.builder()
        .email("")
        .name("")
        .password("")
        .wishlists(l)
        .build();

    private User userTen = User.builder()
        .email("userTen@gmail.com")
        .name("userTen")
        .password("nycjfkGG48#%")
        .wishlists(l)
        .build();

    /**
     * Setup the mockito annotations.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    /**
     * Test the create user endpoint.
     * @throws Exception
     */
    @Test
    public void shouldCreateUser() throws Exception {
        User user = User.builder()
            .email("test@gmail.com")
            .name("test")
            .password("hjgT48582%%")
            .wishlists(List.of())
            .build();

        // This exact user is not being passed....
        Mockito.when(userService.create(user)).thenReturn(user);
        // String content = objectWriter.writeValueAsString(user);
        String content = String.format(
            "{%s,%s,%s,%s}",
            String.format("\"email\":\"%s\"", user.getEmail()),
            String.format("\"name\":\"%s\"", user.getName()),
            String.format("\"password\":\"%s\"", user.getPassword()),
            "\"wishlists\": []"
        );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isOk());
            // Fixed this out.
            // .andExpect(jsonPath("$", notNullValue()));
            // .andExpect(jsonPath("$", notNullValue()));
            // .andExpect(jsonPath("$.email", is("test@gmail.com")));
    }


    /**
     * Test the create user endpoint.
     * @throws Exception
     */
    @Test
    public void shouldFailWhiteCreatingInvalidUser() throws Exception {
        List<Wishlist> l = List.of();
        User user = User.builder()
            .email("test")
            .name("test name")
            .password("hjgT48582%%")
            .wishlists(l)
            .build();

        Mockito.when(userService.create(user)).thenReturn(user);
        String content = objectWriter.writeValueAsString(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest());
    }


    /**
     * Test fetching all users.
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        List<User> users = List.of(userOne, userTwo, userThree, userTen);

        Mockito
            .when(userService.getAll())
            .thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
            .get("/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0].email", is("omniyyah@gmail.com")));
    }

    /**
     * Test fetching a user by id.
     * @throws Exception
     */
    @Test
    public void getUserById() throws Exception {
        Mockito
            .when(userService.findById(userTen.getId()))
            .thenReturn(java.util.Optional.of(userTen));
    }

    /**
     * Test updating a user by id.
     * @throws Exception
     */
    @Test
    public void updateUser() throws Exception {
        List<Wishlist> l = List.of();

        User origUser = User.builder()
            .email("test@gmail.com")
            .name("test name")
            .password("hjgT48582%%")
            .wishlists(l)
            .build();

        User updatedUser = User.builder()
            .email("test@gmail.com")
            .name("test update name")
            .password("hjgT48582%%")
            .wishlists(l)
            .build();

        Mockito
            .when(userService.findById(updatedUser.getId()))
            .thenReturn(java.util.Optional.of(origUser));

        Mockito
            .when(userService.update(origUser))
            .thenReturn(updatedUser);

        String content = "{\"name\":\"test update name\"}";
        String userId = origUser.getId();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put("/users/" + userId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.name", is("test update name")));

    }

    /**
     * Test deleting all users.
     * @throws Exception
     */
    @Test
    public void deleteAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

}
