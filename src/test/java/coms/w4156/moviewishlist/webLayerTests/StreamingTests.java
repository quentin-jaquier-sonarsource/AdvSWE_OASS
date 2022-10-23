package coms.w4156.moviewishlist.webLayerTests;

import coms.w4156.moviewishlist.Controllers.StreamingController;
import coms.w4156.moviewishlist.Services.WatchModeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StreamingController.class)
public class StreamingTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    WatchModeService wms;

    /**
     * Tests our test endpoint. Not interesting, just asserts
     * that it returns what it returns basically
     * @throws Exception
     */
    @Test
    public void mockAvail() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add("Hulu");
        mockList.add("HBO Max");

        Mockito.when(wms.testResponse()).thenReturn(mockList);

        this.mockMvc.perform(get("/streaming/available"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "Hulu")))
                .andDo(print());

        this.mockMvc.perform(get("/streaming/available"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "HBO Max")))
                .andDo(print());
    }

    /**
     * Test that in normal conditions, our endpoint works fine
     * @throws Exception
     */
    @Test
    public void mockGetFreeSubHappyPath() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add("Hulu");
        mockList.add("HBO Max");

        Mockito.when(wms.getFreeWithSubSourcesById("0")).thenReturn(mockList);

        this.mockMvc.perform(get("/streaming/freeWithSub/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "HBO Max")))
                .andDo(print());

        this.mockMvc.perform(get("/streaming/freeWithSub/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "Hulu")))
                .andDo(print());

    }

    @Test
    public void mockGetFreeSubMissingId() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add("Hulu");
        mockList.add("HBO Max");

        Mockito.when(wms.getFreeWithSubSourcesById("0")).thenReturn(mockList);

        this.mockMvc.perform(get("/streaming/freeWithSub"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
