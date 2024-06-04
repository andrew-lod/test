package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueTypeRepository extends JpaRepository<IssueType, String> {
}
