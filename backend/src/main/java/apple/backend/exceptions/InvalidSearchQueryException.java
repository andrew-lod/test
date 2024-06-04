package apple.backend.exceptions;

public class InvalidSearchQueryException extends RuntimeException {
    /* Thrown when the search query contains illegal arguments */
    public InvalidSearchQueryException(String message) {
        super(message);
    }
}
