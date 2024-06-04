package apple.backend.exceptions;

public class TicketsNotFoundException extends RuntimeException {
    /* Thrown when a user searches for a list of tickets but no tickets were found */
    public TicketsNotFoundException() {
        super("No tickets found");
    }
}
