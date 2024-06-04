package apple.backend.exceptions;

public class ImportFileNoNewTicketsException extends RuntimeException {
/* Thrown when UC-1 didn't import any new tickets because the file only contained tickets that already existed */
    public ImportFileNoNewTicketsException() {
        super("No new tickets were imported because the file only contained tickets that already existed");
    }
}
