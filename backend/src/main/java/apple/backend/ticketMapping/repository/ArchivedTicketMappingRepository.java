package apple.backend.ticketMapping.repository;

import apple.backend.ticketMapping.domain.entity.ArchivedTicketMapping;
import apple.backend.ticketMapping.domain.entity.keys.ArchivedTicketMappingId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchivedTicketMappingRepository extends JpaRepository<ArchivedTicketMapping, ArchivedTicketMappingId> {
    List<ArchivedTicketMapping> findByArchivedTicketId(int id);
}
