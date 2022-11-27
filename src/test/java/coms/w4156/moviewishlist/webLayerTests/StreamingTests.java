package coms.w4156.moviewishlist.webLayerTests;

import coms.w4156.moviewishlist.controllers.StreamingController;
import coms.w4156.moviewishlist.services.WatchModeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static coms.w4156.moviewishlist.utils.StreamingConstants.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
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
        mockList.add(HULU_NAME);
        mockList.add(HBO_NAME);

        Mockito.when(wms.testResponse()).thenReturn(mockList);

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_TEST_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        HULU_NAME)))
                .andDo(print());

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_TEST_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        HBO_NAME)))
                .andDo(print());
    }

    /**
     * Test that in normal conditions, our endpoint works fine
     * @throws Exception
     */
    @Test
    public void mockGetFreeSubHappyPath() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add(HULU_NAME);
        mockList.add(HBO_NAME);

        Mockito.when(wms.getFreeWithSubSourcesById("0")).thenReturn(mockList);

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_SUB_ENDPOINT+"/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        HBO_NAME)))
                .andDo(print());

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_SUB_ENDPOINT+"/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        HULU_NAME)))
                .andDo(print());

    }

    /**
     * Test that when you do not supply an id, an error is given
     * @throws Exception
     */
    @Test
    public void mockGetFreeSubMissingId() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add(HULU_NAME);
        mockList.add(HBO_NAME);

        Mockito.when(wms.getFreeWithSubSourcesById("0")).thenReturn(mockList);

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_SUB_ENDPOINT))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    /**
     * Test that in normal conditions, the rent endpoint works fine
     * @throws Exception
     */
    @Test
    public void mockGetBuyHappyPath() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add(VUDU_NAME);
        mockList.add(APPLE_TV_NAME);

        Mockito.when(wms.getBuySourcesById("0")).thenReturn(mockList);

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_BUY_ENDPOINT+"/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        VUDU_NAME)))
                .andDo(print());

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_BUY_ENDPOINT+"/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        APPLE_TV_NAME)))
                .andDo(print());

    }

    /**
     * Test that when you do not supply an id, an error is given
     * @throws Exception
     */
    @Test
    public void mockGetBuyMissingId() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add(HULU_NAME);
        mockList.add(HBO_NAME);

        Mockito.when(wms.getBuySourcesById("0")).thenReturn(mockList);

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_BUY_ENDPOINT))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    /**
     * Test that in normal conditions, the rent endpoint works fine
     * @throws Exception
     */
    @Test
    public void mockGetRentHappyPath() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add(VUDU_NAME);
        mockList.add(APPLE_TV_NAME);

        Mockito.when(wms.getRentSourcesById("0")).thenReturn(mockList);

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_RENT_ENDPOINT+"/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        VUDU_NAME)))
                .andDo(print());

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_RENT_ENDPOINT+"/0"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        APPLE_TV_NAME)))
                .andDo(print());

    }

    /**
     * Test that when you do not supply an id, an error is given
     * @throws Exception
     */
    @Test
    public void mockGetRentMissingId() throws Exception {

        List<String> mockList = new ArrayList<>();
        mockList.add(HULU_NAME);
        mockList.add(HBO_NAME);

        Mockito.when(wms.getRentSourcesById("0")).thenReturn(mockList);

        this.mockMvc.perform(get(STREAMING_BASE+STREAMING_RENT_ENDPOINT))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
