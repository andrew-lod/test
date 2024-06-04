package apple.backend.ticketManagement.api.exception;

public class ImportFileUnsupportedFormatException extends RuntimeException {
    public ImportFileUnsupportedFormatException(String message) {
        super(message);
    }
}
