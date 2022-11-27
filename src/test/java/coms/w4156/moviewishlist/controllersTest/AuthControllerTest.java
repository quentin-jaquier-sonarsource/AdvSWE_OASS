package coms.w4156.moviewishlist.controllersTest;
import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.ProfileService;
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

import coms.w4156.moviewishlist.controllers.AuthController;
import coms.w4156.moviewishlist.controllers.ProfileController;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

// @RunWith(MockitoJUnitRunner.Silent.class)
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private AuthController authController;


    private Client client = Client.builder()
        .email("client@gmail.com")
        .build();

    private String clientJwt;

    /**
     * Setup the mockito annotations.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    /**
     * Test the create client endpoint.
     * @throws Exception
     */
    @Test
    public void shouldCreateClientTest() throws Exception {
        Mockito.when(clientService.create(client)).thenReturn(client);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/new-client?email=" + client.getEmail())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.jwtToken", notNullValue()))
            .andReturn();

        this.clientJwt = result.getResponse().getContentAsString();

        System.out.println(this.clientJwt);
    }

    /**
     * Test that endpoints cannot be accessed without the token
     */
    @Test
    public void shouldForbidUnauthenticatedRequestsTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
            .andExpect(status().isForbidden());
    }

    /**
     * Test that endpoints can be accessed with the token
     */
    @Test
    public void shouldAllowAuthenticatedRequestsTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + this.clientJwt)
            .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }
}
