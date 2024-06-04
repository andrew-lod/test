package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, String>, JpaSpecificationExecutor<Ticket> {
    @Transactional
    @Modifying
    @Query("UPDATE Ticket t SET t = :ticket WHERE t.issueKey = :issueKey")
    void detach(Ticket ticket, String issueKey);
}
