package apple.backend.exceptions;

public class ImportFileNotParsableException extends RuntimeException {
    /* Thrown when system was unable to parse the ImportFile  */
    public ImportFileNotParsableException(String message) {
        super(message);
    }
}
