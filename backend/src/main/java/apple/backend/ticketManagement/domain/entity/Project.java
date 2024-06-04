package apple.backend.ticketManagement.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project")
public class Project {
    @Id
    private String key;
    private String name;
    private String type;
    private String description;
    private String url;

    @ManyToOne
    @JoinColumn(name = "lead")
    private UserReference lead;

    public Project() {
    }

    public Project(String name, String type, String description, String url, UserReference lead) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.url = url;
        this.lead = lead;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserReference getLead() {
        return lead;
    }

    public void setLead(UserReference lead) {
        this.lead = lead;
    }
}
