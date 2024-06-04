package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.Comment;
import apple.backend.ticketManagement.domain.entity.keys.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, CommentId> {
}
