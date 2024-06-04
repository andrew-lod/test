package apple.backend.ticketManagement.domain.entity;

import apple.backend.ticketManagement.domain.entity.keys.CustomValueId;
import jakarta.persistence.*;

@Entity
@IdClass(CustomValueId.class)
@Table(name = "customvalue")
public class CustomValue {
    @Id
    @ManyToOne
    @JoinColumn(name="key")
    private CustomAttribute customAttribute;

    @Id
    private String value;

    @Id
    @ManyToOne
    @JoinColumn(name="issue_key")
    private Ticket ticket;

    public CustomAttribute getCustomAttribute() {
        return customAttribute;
    }

    public void setCustomAttribute(CustomAttribute customAttribute) {
        this.customAttribute = customAttribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
