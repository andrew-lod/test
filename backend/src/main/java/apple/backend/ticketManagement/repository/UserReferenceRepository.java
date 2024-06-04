package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.UserReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReferenceRepository extends JpaRepository<UserReference, String>{
}
