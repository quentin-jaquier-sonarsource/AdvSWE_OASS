package coms.w4156.moviewishlist.exceptions;

public class ClientAlreadyExistsException extends Exception {

    /**
     * Create a new empty error.
     */
    public ClientAlreadyExistsException() {}

    /**
     * Create an error with the given message.
     * @param message - the Error message.
     */
    public ClientAlreadyExistsException(final String message) {
        super(message);
    }
}
