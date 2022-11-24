package coms.w4156.moviewishlist.exceptions;

public class InvalidJwtException extends Exception {
    public InvalidJwtException() {}

    public InvalidJwtException(String message) {
        super(message);
    }
}
