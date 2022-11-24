package coms.w4156.moviewishlist.exceptions;

public class ClientAlreadyExistsException extends Exception {
    public ClientAlreadyExistsException() {}

    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
