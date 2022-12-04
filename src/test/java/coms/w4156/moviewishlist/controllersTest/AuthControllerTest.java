package coms.w4156.moviewishlist.controllersTest;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.services.ClientService;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.JsonPath;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class AuthControllerTest {
    /**
     * Used to test without having to run the entire app
     */
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private Client client = Client.builder()
        .email("client@gmail.com")
        .build();

        private String clientJwt = "";

        /**
     * Test the create client endpoint.
     * @throws Exception
     */
        @Test
        public void shouldCreateClientTest() throws Exception {
                UserDetails clientDetails = new org.springframework.security.core.userdetails.User(client.getEmail(), "", new ArrayList<>());
                Mockito.when(clientService.create(client)).thenReturn(client);
                Mockito.when(clientService.createClientAndReturnDetails(client.getEmail())).thenReturn(clientDetails);


                MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/new-client?email=" + client.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);

                MvcResult result = mockMvc.perform(request)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token", notNullValue()))
                        .andReturn();

                this.clientJwt = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
        }


        /**
     * Test that endpoints cannot be accessed without the token
     */
    @Test
    public void shouldForbidUnauthenticatedRequestsTest() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/graphql")
            .content("{\"query\": query { profiles { id } } }")
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
            .content("{\"query\": query { profiles { id } } }")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + this.clientJwt)
            .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
            .andExpect(status().isOk());
    }
}
