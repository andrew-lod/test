package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.ArchivedTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivedTicketRepository extends JpaRepository<ArchivedTicket, Integer> {
}
