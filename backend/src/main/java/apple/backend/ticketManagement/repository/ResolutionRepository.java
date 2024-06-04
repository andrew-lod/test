package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResolutionRepository extends JpaRepository<Resolution, String>{
}
