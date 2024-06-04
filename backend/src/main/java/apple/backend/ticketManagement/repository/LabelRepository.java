package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, String> {
}
