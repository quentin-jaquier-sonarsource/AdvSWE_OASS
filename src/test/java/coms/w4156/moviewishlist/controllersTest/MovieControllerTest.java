package coms.w4156.moviewishlist.controllersTest;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.User;
import coms.w4156.moviewishlist.services.MovieService;
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
import coms.w4156.moviewishlist.controllers.MovieController;
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
public class MovieControllerTest {

    private MockMvc mockMvc;

    //we need to convert from json to string and vice versa
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    //Mock movie repo
    @MockBean
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    Movie movie1 = new Movie("movie1", 2001);
    Movie movie2 = new Movie("movie2", 1995);
    Movie movie3 = new Movie("", 0);
    Movie movie4 = new Movie("",00);
    Movie movie5 = new Movie("",000);
    Movie movie6 = new Movie("",0000);
    Movie movie7 = new Movie("notoot",103994);

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }


    @Test
    public void createMovie_success() throws Exception{
        Movie movie = Movie.builder()
                .title("test title")
                .releaseYear(1995)
                .build();

        Mockito.when(movieService.create(movie)).thenReturn(movie);
        String content = objectWriter.writeValueAsString(movie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.releaseYear", is("test releaseYear")));
    }

    @Test
    public void createMovie_fail() throws Exception{
        Movie movie = Movie.builder()
                .title("")
                .releaseYear("test releaseYear")
                .build();

        Mockito.when(movieService.create(movie)).thenReturn(movie);
        String content = objectWriter.writeValueAsString(movie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAll() throws Exception{
        List<Movie> movies = new ArrayList<>(Arrays.asList(movie1,
                movie2, movie3, movie4, movie5, movie6, movie7));

        Mockito.when(movieService.getAll()).thatReturn(movies);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/movies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$[1].title", is("movie2")));
    }


    @Test
    public void getMovieById() throws Exception{
        Mockito.when(movieService.findById(movie1.getId()))
                .thenReturn(java.util.Optional.of(movie1));
    }

    @Test
    public void updateMovie() throws Exception{

        Movie updatedMovie = Movie.builder()
                .title("test update title")
                .releaseYear("test update releaseYear")
                .build();

        Mockito.when(movieService.findById(updatedMovie.getId())).thenReturn(java.util.Optional.of(updatedMovie));
        Mockito.when(movieService.update(updatedMovie)).thenReturn(updatedMovie);

        String content = objectWriter.writeValueAsString(updatedMovie);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.title", is("test update title")));

    }


    @Test
    public void deleteAllMovies() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/movies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
