package apple.backend.ticketManagement.domain.entity.keys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CommentId implements Serializable {
    private String ticket;
    private String body;

    public CommentId() {
    }
    public CommentId(String ticket, String body) {
        this.ticket = ticket;
        this.body = body;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String comment) {
        this.body = comment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket, body);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CommentId that = (CommentId) obj;
        return Objects.equals(ticket, that.ticket) && Objects.equals(body, that.body);
    }
}
