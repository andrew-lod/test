package apple.backend.ticketManagement.domain.entity;

import apple.backend.ticketManagement.domain.entity.keys.AttachmentId;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "attachment")
@IdClass(AttachmentId.class)
public class Attachment {
    @Id
    @ManyToOne
    @JoinColumn(name = "issue_key")
    private Ticket ticket;

    @Id
    private String name;

    @Id
    @Column(name = "upload_date")
    private Date uploadedAt;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserReference uploader;

    private String url;

    public Attachment() {
    }

    public Attachment(String name, Date uploadedAt, UserReference uploader, String url) {
        this.name = name;
        this.uploadedAt = uploadedAt;
        this.uploader = uploader;
        this.url = url;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public UserReference getUploader() {
        return uploader;
    }

    public void setUploader(UserReference uploader) {
        this.uploader = uploader;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
