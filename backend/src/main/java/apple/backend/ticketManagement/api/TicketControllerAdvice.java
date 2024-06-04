package apple.backend.ticketManagement.api;

import apple.backend.ticketManagement.api.exception.TicketNotFoundException;
import apple.backend.ticketManagement.api.exception.ImportFileNoNewTicketsException;
import apple.backend.ticketManagement.api.exception.ImportFileUnsupportedFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketControllerAdvice {

    /* -------------------- Get tickets -------------------- */

    /**
     * Handles the {@link TicketNotFoundException} exception
     *
     * @param ex the {@link TicketNotFoundException} exception that was thrown
     *
     * @return An error message
     */
    @ExceptionHandler(TicketNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleTicketsNotFoundException(TicketNotFoundException ex) {
        return ex.getMessage();
    }
    /* -------------------- Import tickets -------------------- */
    /**
     * Handles the {@link ImportFileUnsupportedFormatException} thrown during file import operations.
     * <p>
     * Responds with an HTTP 400 (Bad Request) status code and a descriptive error message from the exception.
     *
     * @param ex the encountered exception detailing the unsupported file format issue
     * @return the descriptive error message for the client
     */
    @ExceptionHandler(ImportFileUnsupportedFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleImportFileUnsupportedFormatException(ImportFileUnsupportedFormatException ex) {
        return ex.getMessage();
    }

    /**
     * Handles the {@link ImportFileNoNewTicketsException} thrown during file import operations.
     * <p>
     * Responds with an HTTP 400 (Bad Request) status code and a descriptive error message from the exception.
     *
     * @param ex the encountered exception detailing the unsupported file format issue
     * @return the descriptive error message for the client
     */
    @ExceptionHandler(ImportFileNoNewTicketsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleImportFileNoNewTicketsException(ImportFileNoNewTicketsException ex) {
        return ex.getMessage();
    }
}
