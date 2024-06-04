package apple.backend.ticketManagement.api.exception;

public class ImportFileNoNewTicketsException extends RuntimeException {
    public static final String MESSAGE = "The import file does not contain any new tickets.";

    public ImportFileNoNewTicketsException() {
        super(MESSAGE);
    }
}
