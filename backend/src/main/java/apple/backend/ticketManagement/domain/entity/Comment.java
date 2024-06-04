package apple.backend.ticketManagement.domain.entity;

import apple.backend.ticketManagement.domain.entity.keys.CommentId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import apple.backend.ticketManagement.domain.entity.keys.CommentId;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "comment")
@IdClass(CommentId.class)
public class Comment {
    @Id
    @ManyToOne
    @JoinColumn(name = "issue_key")
    @JsonIgnore
    private Ticket ticket;

    @Id
    @Column(name = "comment")
    private String body;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserReference author;

    @Column(name = "comment_date")
    private Date createdAt;

    public Comment() {
    }

    public Comment(String body, UserReference author, Date createdAt) {
        this.body = body;
        this.author = author;
        this.createdAt = createdAt;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String comment) {
        this.body = comment;
    }

    public UserReference getAuthor() {
        return author;
    }

    public void setAuthor(UserReference commenter) {
        this.author = commenter;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date commentedAt) {
        this.createdAt = commentedAt;
    }
}
