package apple.backend.filter.repository;

import apple.backend.filter.domain.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Integer> {
}
