package apple.backend.ticketManagement.api.exception;

import java.util.function.Supplier;

public class TicketNotFoundException extends RuntimeException implements Supplier<TicketNotFoundException> {

    /* Thrown when a user searches for a list of tickets but no tickets were found */
    public TicketNotFoundException(String id) {
        super("Ticket with id: " + id + " was not found");
    }

    @Override
    public TicketNotFoundException get() {
        return this;
    }
}
