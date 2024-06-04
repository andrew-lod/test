package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.CustomAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomAttributeRepository extends JpaRepository<CustomAttribute, String> {
}
