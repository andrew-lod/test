package apple.backend.ticketManagement.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "archivedobject")
public class ArchivedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "object")
    private String ticketObject;

    private String mediaType;

    public ArchivedTicket(String object) {
        this.ticketObject = object;
    }

    // Constructors
    public ArchivedTicket() {
    }

    public ArchivedTicket(String object, String mediaType) {
        this.ticketObject = object;
        this.mediaType = mediaType;
    }

    public ArchivedTicket(int id, String object, String contentType) {
        this.id = id;
        this.ticketObject = object;
        this.mediaType = contentType;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicketObject() {
        return ticketObject;
    }

    public void setTicketObject(String ticketObject) {
        this.ticketObject = ticketObject;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
