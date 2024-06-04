package apple.backend.pinManagement.repository;
import apple.backend.pinManagement.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, String>{
}