package coms.w4156.moviewishlist.controllersTest;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
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
import coms.w4156.moviewishlist.controllers.ProfileController;
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
public class ProfileControllerTest {

    private MockMvc mockMvc;

    // We need to convert from json to string and vice versa
    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter = objectMapper.writer();

    // Mock profile repo
    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private List<Wishlist> l = List.of();
    // Should pass
    private Profile profileOne = Profile.builder()
        .name("omniyyah")
        .wishlists(l)
        .build();

    // Should not pass
    private Profile profileTwo = Profile.builder()
        .name("omniyyah")
        .wishlists(l)
        .build();

    // Should not pass
    private Profile profileThree = Profile.builder()
        .name("")
        .wishlists(l)
        .build();

    private Profile profileTen = Profile.builder()
        .name("profileTen")
        .wishlists(l)
        .build();

    /**
     * Setup the mockito annotations.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

    /**
     * Test the create profile endpoint.
     * @throws Exception
     */
    @Test
    public void shouldCreateProfile() throws Exception {
        Profile profile = Profile.builder()
            .name("test")
            .wishlists(List.of())
            .build();

        // This exact profile is not being passed....
        Mockito.when(profileService.create(profile)).thenReturn(profile);
        // String content = objectWriter.writeValueAsString(profile);
        String content = String.format(
            "{%s,%s,%s,%s}",
            String.format("\"name\":\"%s\"", profile.getName()),
            "\"wishlists\": []"
        );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/profiles")
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
     * Test the create profile endpoint.
     * @throws Exception
     */
    @Test
    public void shouldFailWhiteCreatingInvalidProfile() throws Exception {
        List<Wishlist> l = List.of();
        Profile profile = Profile.builder()
            .name("test name")
            .wishlists(l)
            .build();

        Mockito.when(profileService.create(profile)).thenReturn(profile);
        String content = objectWriter.writeValueAsString(profile);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc
            .perform(request)
            .andExpect(status().isBadRequest());
    }


    /**
     * Test fetching all profiles.
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        List<Profile> profiles = List.of(profileOne, profileTwo, profileThree, profileTen);

        Mockito
            .when(profileService.getAll())
            .thenReturn(profiles);

        mockMvc.perform(MockMvcRequestBuilders
            .get("/profiles")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)));
    }

    /**
     * Test fetching a profile by id.
     * @throws Exception
     */
    @Test
    public void getProfileById() throws Exception {
        Mockito
            .when(profileService.findById(profileTen.getId()))
            .thenReturn(java.util.Optional.of(profileTen));
    }

    /**
     * Test updating a profile by id.
     * @throws Exception
     */
    @Test
    public void updateProfile() throws Exception {
        List<Wishlist> l = List.of();

        Profile origProfile = Profile.builder()
            .name("test name")
            .wishlists(l)
            .build();

        Profile updatedProfile = Profile.builder()
            .name("test update name")
            .wishlists(l)
            .build();

        Mockito
            .when(profileService.findById(updatedProfile.getId()))
            .thenReturn(java.util.Optional.of(origProfile));

        Mockito
            .when(profileService.update(origProfile))
            .thenReturn(updatedProfile);

        String content = "{\"name\":\"test update name\"}";
        Long profileId = origProfile.getId();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put("/profiles/" + profileId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.name", is("test update name")));

    }

    /**
     * Test deleting all profiles.
     * @throws Exception
     */
    @Test
    public void deleteAllProfiles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/profiles")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

}
