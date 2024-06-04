package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.CustomValue;
import apple.backend.ticketManagement.domain.entity.keys.CustomValueId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomValueRespository extends JpaRepository<CustomValue, CustomValueId> {
}
