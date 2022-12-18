package coms.w4156.moviewishlist.jwtTests;

import coms.w4156.moviewishlist.security.jwt.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
class JwtTokenUtilTests {

    @Mock
    public JwtTokenUtil jwtTokenUtil;

    @Test
    void testGetExpiration() {
        String token = "token";
        Date date = new Date();

        Mockito
                .when(jwtTokenUtil.getExpirationDateFromToken(token))
                .thenReturn(date);

        Assertions.assertEquals(date, jwtTokenUtil.getExpirationDateFromToken(token));
    }
}
