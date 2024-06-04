package apple.backend.ticketMapping.repository;

import apple.backend.ticketMapping.domain.entity.GlobalMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalMappingRepository extends JpaRepository<GlobalMapping, String> {
}
