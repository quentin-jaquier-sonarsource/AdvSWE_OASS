package coms.w4156.moviewishlist.jwtTests;

import coms.w4156.moviewishlist.security.jwt.JwtRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtRequestTests {

    @Test
    void test() {
        JwtRequest jwtRequest = new JwtRequest();
        JwtRequest jwtRequest2 = new JwtRequest();

        Assertions.assertNotEquals(jwtRequest, jwtRequest2);
    }

    @Test
    void testConstructorAndGetEmail() {

        String email = "email";
        String password = "password";
        JwtRequest jwtRequest = new JwtRequest(email, password);

        Assertions.assertEquals(email, jwtRequest.getEmail());
    }

    @Test
    void testSetEmail() {

        String email = "email";
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setEmail(email);

        Assertions.assertEquals(email, jwtRequest.getEmail());
    }
}
