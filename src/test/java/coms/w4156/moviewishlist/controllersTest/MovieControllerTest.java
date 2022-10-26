package coms.w4156.moviewishlist.controllersTest;
import coms.w4156.moviewishlist.models.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import coms.w4156.moviewishlist.services.MovieService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import coms.w4156.moviewishlist.controllers.MovieController;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.http.MediaType;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MovieControllerTest {

    /**
     * Mocks MVC for testing endpoints.
     */
    private MockMvc mockMvc;

    /**
     * Utility to convert from Java objects to JSON and back.
     */
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Utility to convert from Java objects to JSON.
     */
    private ObjectWriter objectWriter = objectMapper.writer();

    /**
     * Mocks the movie service.
     */
    @Mock
    private MovieService movieService;

    /**
     * Injects the movie controller with the mocked services.
     */
    @InjectMocks
    private MovieController movieController;

    /**
     * Mock data for testing.
     */
    private Movie movie1 = Movie.builder()
        .title("movie1")
        .releaseYear(2001)
        .build();

    /**
     * Mock data for testing.
     */
    private Movie movie2 = Movie.builder()
        .title("movie2")
        .releaseYear(1995)
        .build();

    /**
     * Mock data for testing.
     */
    private Movie movie3 = Movie.builder()
        .title("")
        .releaseYear(2003)
        .build();

    /**
     * Setup for testing.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }


    /**
     * Tests creating a movie.
     * @throws Exception
     */
    @Test
    public void shouldCreateMovie() throws Exception {
        Movie movie = Movie.builder()
            .title("test title")
            .releaseYear(1995)
            .build();

        Mockito.when(movieService.create(movie)).thenReturn(movie);
        String content = objectWriter.writeValueAsString(movie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.releaseYear", is(1995)));
    }

    /**
     * Tests creating an invalid movie.
     * @throws Exception
     */
    @Test
    public void shouldFailTryingToCreateAnInvalidMovie() throws Exception {
        Movie movie = Movie.builder()
            .title("")
            .releaseYear(2009)
            .build();

        Mockito.when(movieService.create(movie)).thenReturn(movie);
        String content = objectWriter.writeValueAsString(movie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/movies")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isBadRequest());
    }

    /**
     * Tests getting all movies.
     * @throws Exception
     */
    @Test
    public void getAll() throws Exception {
        List<Movie> movies = List.of(movie1, movie2, movie3);

        Mockito.when(movieService.getAll()).thenReturn(movies);

        mockMvc.perform(MockMvcRequestBuilders
            .get("/movies")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[1].title", is("movie2")));
    }

    /**
     * Tests getting a movie by id.
     * @throws Exception
     */
    @Test
    public void getMovieById() throws Exception {
        Mockito
            .when(movieService.findById(movie1.getId()))
            .thenReturn(java.util.Optional.of(movie1));
    }

    /**
     * Tests updating a movie.
     * @throws Exception
     */
    @Test
    public void updateMovie() throws Exception {

        Movie movie = Movie.builder()
            .title("Unmodified Movie")
            .releaseYear(1995)
            .build();

        String content =
            "{\"title\":\"test update title\",\"releaseYear\":2018}";

        Movie updatedMovie = Movie.builder()
            .title("test update title")
            .releaseYear(2018)
            .build();

        Long movieId = 9L;

        Mockito
            .when(movieService.findById(movieId))
            .thenReturn(java.util.Optional.of(movie));

        Mockito
            .when(movieService.update(movie))
            .thenReturn(updatedMovie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put(String.format("/movies/%d", movieId))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(content);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", notNullValue()))
            .andExpect(jsonPath("$.title", is("test update title")));

    }

    /**
     * Tests deleting all movies.
     * @throws Exception
     */
    @Test
    public void deleteAllMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .delete("/movies")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
