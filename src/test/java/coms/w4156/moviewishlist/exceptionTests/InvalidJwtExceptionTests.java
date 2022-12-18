package coms.w4156.moviewishlist.exceptionTests;

import coms.w4156.moviewishlist.exceptions.ClientAlreadyExistsException;
import coms.w4156.moviewishlist.exceptions.InvalidJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvalidJwtExceptionTests {
    @Test
    void test() {
        InvalidJwtException existsException  = new InvalidJwtException();
        InvalidJwtException existsException2  = new InvalidJwtException();

        Assertions.assertNotEquals(existsException, existsException2);
    }

    void test2() {
        String msg = "msg";
        InvalidJwtException existsException  = new InvalidJwtException(msg);
        InvalidJwtException existsException2  = new InvalidJwtException(msg);

        Assertions.assertNotEquals(existsException, existsException2);
    }
}
