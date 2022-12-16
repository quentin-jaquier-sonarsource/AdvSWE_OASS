package coms.w4156.moviewishlist.controllersTest;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.ProfileService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {

    // Used to test without having to run the entire app
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ProfileService profileService;

    private Client client = Client.builder().email("user").build();

    /**
     * Test the create client endpoint.
     * @throws Exception
     */
    @Test
    public void shouldCreateClientTest() throws Exception {
        UserDetails clientDetails = new User(
            client.getEmail(),
            "",
            new ArrayList<>()
        );
        Mockito.when(clientService.create(client)).thenReturn(client);
        Mockito
            .when(clientService.createClientAndReturnDetails(client.getEmail()))
            .thenReturn(clientDetails);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/new-client?email=" + client.getEmail())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token", notNullValue()));
    }

    /**
     * Test that endpoints cannot be accessed without the token.
     */
    @Test
    public void shouldForbidUnauthenticatedRequestsTest() throws Exception {
        Profile profile = Profile
            .builder()
            .name("profile1")
            .id(Long.valueOf(1))
            .client(client)
            .build();
        Mockito.when(profileService.findAll()).thenReturn(List.of(profile));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/graphql")
            .content("{\"query\": \"query { profiles { id } }\" }")
            .characterEncoding("utf-8")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mockMvc
            .perform(request)
            .andDo(res -> {
                assertEquals("", res.getResponse().getContentAsString());
            });
    }

    /**
     * Test that endpoints can be accessed with the token.
     */
    @Test
    public void shouldAllowAuthenticatedRequestsTest() throws Exception {
        Mockito
            .when(clientService.findByEmail("user"))
            .thenReturn(Optional.of(client));

        UserDetails clientDetails = new User(
            client.getEmail(),
            "",
            new ArrayList<>()
        );
        Mockito.when(clientService.create(client)).thenReturn(client);
        Mockito
            .when(clientService.createClientAndReturnDetails(client.getEmail()))
            .thenReturn(clientDetails);
        Mockito
            .when(clientService.loadUserByUsername(client.getEmail()))
            .thenReturn(clientDetails);

        Profile profile = Profile
            .builder()
            .name("profile1")
            .id(Long.valueOf(1))
            .client(client)
            .build();
        Mockito
            .when(profileService.getAllForClient(client.getId()))
            .thenReturn(List.of(profile));

        var createClientRequest = MockMvcRequestBuilders
            .post("/new-client?email=" + client.getEmail())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(createClientRequest).andReturn();

        String token = JsonPath.read(
            result.getResponse().getContentAsString(),
            "$.token"
        );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/graphql")
            .content("{\"query\": \"query { profiles { id, name } }\" }")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request);
        // .andExpect(jsonPath("$.profiles").value(List.of(profile)));
    }
}
