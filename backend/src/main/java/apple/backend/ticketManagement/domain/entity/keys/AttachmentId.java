package apple.backend.ticketManagement.domain.entity.keys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class AttachmentId implements Serializable {
    private String ticket;
    private String name;
    private Date uploadedAt;

    public AttachmentId() {
    }

    public AttachmentId(String ticket, String name, Date uploadedAt) {
        this.ticket = ticket;
        this.name = name;
        this.uploadedAt = uploadedAt;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket, name, uploadedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AttachmentId that = (AttachmentId) obj;
        return Objects.equals(ticket, that.ticket) && Objects.equals(name, that.name) && Objects.equals(uploadedAt, that.uploadedAt);
    }
}
