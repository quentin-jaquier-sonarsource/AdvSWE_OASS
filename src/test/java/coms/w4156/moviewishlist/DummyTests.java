package coms.w4156.moviewishlist;

import coms.w4156.moviewishlist.services.DummyService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class DummyTests {
    /**
     * Used to test without having to run the entire app
     */
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    DummyService ds;

    @Test
    public void numTest() throws Exception {
        this.mockMvc.perform(get("/num"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "5")))
                .andDo(print());
    }
    @Disabled
    @Test
    public void mockNumTest() throws Exception {

        Mockito.when(ds.getNum()).thenReturn("4");

        this.mockMvc.perform(get("/num"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "4")))
                .andDo(print());
    }

    @Test
    public void testEndpointTest() throws Exception {
        this.mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "All good!")))
                .andDo(print());
    }


    @Test
    public void randTest() throws Exception {
        this.mockMvc.perform(get("/rand"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "0.")))
                .andDo(print());
    }


}
