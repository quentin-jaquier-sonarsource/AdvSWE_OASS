package coms.w4156.moviewishlist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import coms.w4156.moviewishlist.security.jwt.JwtTokenUtil;

@SpringBootTest(classes = {
    JwtTokenUtil.class
})
class MovieWishlistApplicationTests {

    @Test
    void contextLoads() {
    }

}
