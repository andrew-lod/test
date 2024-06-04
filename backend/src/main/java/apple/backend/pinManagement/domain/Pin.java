package apple.backend.pinManagement.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Pin")
public class Pin {

    @Id
    @Column(name = "issue_key")
    private String ticketIssueKey;

    public Pin(String ticketIssueKey) {
        this.ticketIssueKey = ticketIssueKey;
    }

    public Pin() {
    }

    public String getTicketIssueKey() {
        return ticketIssueKey;
    }
}