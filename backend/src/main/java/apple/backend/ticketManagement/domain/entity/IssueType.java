package apple.backend.ticketManagement.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "issuetype")
public class IssueType {
    @Id
    private String name;

    public IssueType() {
    }

    public IssueType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
