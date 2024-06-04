package apple.backend.ticketManagement.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ArchivedTicketController {
    private IArchivedTicketService archivedTicketService;

    @Autowired
    public ArchivedTicketController(IArchivedTicketService archivedTicketService) {
        this.archivedTicketService = archivedTicketService;
    }

    @GetMapping("/archived-tickets/{id}")
    public String getArchivedTicket(@PathVariable String id) {
        return archivedTicketService.getArchivedTicket(Integer.parseInt(id));
    }

}
