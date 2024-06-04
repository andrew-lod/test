package apple.backend.ticketMapping.repository;

import apple.backend.ticketMapping.domain.entity.CustomAttributeMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomAttributeMappingRepository extends JpaRepository<CustomAttributeMapping, Long> {
}
