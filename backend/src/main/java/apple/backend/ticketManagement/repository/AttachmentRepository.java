package apple.backend.ticketManagement.repository;

import apple.backend.ticketManagement.domain.entity.Attachment;
import apple.backend.ticketManagement.domain.entity.keys.AttachmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, AttachmentId> {
}
