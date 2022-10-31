package coms.w4156.moviewishlist.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {}

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
