package apple.backend.ticketMapping.domain.entity;

import apple.backend.ticketManagement.domain.entity.ArchivedTicket;
import apple.backend.ticketMapping.domain.entity.keys.ArchivedTicketMappingId;
import jakarta.persistence.*;

@Entity
@Table(name = "archivedobjectmapping")
@IdClass(ArchivedTicketMappingId.class)
public class ArchivedTicketMapping {
    @Id
    @ManyToOne
    @JoinColumn(name = "object_id")
    private ArchivedTicket archivedTicket;

    @Id
    @Column(name = "\"key\"")
    private String key;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public ArchivedTicketMapping() {
    }

    public ArchivedTicketMapping(ArchivedTicket archivedTicket) {
        this.archivedTicket = archivedTicket;
    }

    public ArchivedTicketMapping(ArchivedTicket archivedTicket, Route route) {
        this.archivedTicket = archivedTicket;
        this.route = route;
    }

    public ArchivedTicket getArchivedTicket() {
        return archivedTicket;
    }

    public void setArchivedTicket(ArchivedTicket archivedTicket) {
        this.archivedTicket = archivedTicket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
