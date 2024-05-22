package ru.baydak.exception;

/**
 * Exception class representing an authorization-related error.
 * Thrown when there is an issue during user authorization.
 */
public class SecurityException extends RuntimeException {

    /**
     * Constructs a new AuthorizeException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public SecurityException(String message) {
        super(message);
    }
}
