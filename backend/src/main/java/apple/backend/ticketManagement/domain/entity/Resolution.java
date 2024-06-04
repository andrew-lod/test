package apple.backend.ticketManagement.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "resolution")
public class Resolution {
    @Id
    private String name;

    public Resolution() {
    }

    public Resolution(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
