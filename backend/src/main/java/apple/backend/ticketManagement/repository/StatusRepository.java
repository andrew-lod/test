package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, String> {
}
