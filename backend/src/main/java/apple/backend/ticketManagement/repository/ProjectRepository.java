package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}
