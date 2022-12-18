package coms.w4156.moviewishlist.exceptionTests;

import coms.w4156.moviewishlist.exceptions.ClientAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClientAlreadyExistsExceptionTest {

    @Test
    void test() {
        ClientAlreadyExistsException existsException  = new ClientAlreadyExistsException();
        ClientAlreadyExistsException existsException2  = new ClientAlreadyExistsException();

        Assertions.assertNotEquals(existsException, existsException2);
    }

}
