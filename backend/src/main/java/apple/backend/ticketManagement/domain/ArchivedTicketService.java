package apple.backend.ticketManagement.domain;

import apple.backend.ticketManagement.api.IArchivedTicketService;
import apple.backend.ticketManagement.api.exception.TicketNotFoundException;
import apple.backend.ticketManagement.repository.ArchivedTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchivedTicketService implements IArchivedTicketService {
    private ArchivedTicketRepository archivedTicketRepository;

    @Autowired
    public ArchivedTicketService(ArchivedTicketRepository archivedTicketRepository) {
        this.archivedTicketRepository = archivedTicketRepository;
    }

    @Override
    public String getArchivedTicket(int id) {
        return archivedTicketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(Integer.toString(id)))
                .getTicketObject();
    }
}
