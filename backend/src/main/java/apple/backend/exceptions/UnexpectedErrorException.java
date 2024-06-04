package apple.backend.exceptions;

public class UnexpectedErrorException extends RuntimeException {
    /* Error thrown when an error is not really relevant for the user so a more generalised error is
    * given, examples: SQLDatabase couldn't connect or error with the query
    *
    * For most exceptions a new specific exception should be created, example SQLError because nothing was found
    * should be more like No[Object]FoundException  */
    public UnexpectedErrorException() {
        super("An unexpected error occurred");
    }
}
