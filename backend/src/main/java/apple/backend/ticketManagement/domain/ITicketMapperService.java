package apple.backend.ticketManagement.domain;

import apple.backend.ticketManagement.domain.entity.Ticket;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITicketMapperService {
    List<Ticket> createTickets(MultipartFile file);
    void populateTickets(List<Ticket> tickets);
}