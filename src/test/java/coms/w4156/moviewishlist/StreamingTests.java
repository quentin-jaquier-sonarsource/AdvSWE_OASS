package coms.w4156.moviewishlist;

import coms.w4156.moviewishlist.Controllers.StreamingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StreamingController.class)
public class StreamingTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAvailable() {
//        TODO
    }

}
