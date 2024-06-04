package apple.backend.ticketMapping.repository;

import apple.backend.ticketMapping.domain.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
