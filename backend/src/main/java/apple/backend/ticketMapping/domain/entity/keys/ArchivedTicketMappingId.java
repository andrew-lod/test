package apple.backend.ticketMapping.domain.entity.keys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ArchivedTicketMappingId implements Serializable {
    private int archivedTicket;
    private String key;

    public ArchivedTicketMappingId() {
    }

    public ArchivedTicketMappingId(int archivedTicket, String key) {
        this.archivedTicket = archivedTicket;
        this.key = key;
    }

    public ArchivedTicketMappingId(int archivedTicketId) {
        this.archivedTicket = archivedTicketId;
    }

    public int getArchivedTicket() {
        return archivedTicket;
    }

    public void setArchivedTicket(int archivedTicket) {
        this.archivedTicket = archivedTicket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(archivedTicket, key);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ArchivedTicketMappingId that = (ArchivedTicketMappingId) obj;
        return Objects.equals(archivedTicket, that.archivedTicket) && Objects.equals(key, that.key);
    }
}
