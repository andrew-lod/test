package apple.backend.ticketManagement.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customattribute")
public class CustomAttribute {
    @Id
    private String key;

    public CustomAttribute(String key) {
        this.key = key;
    }

    public CustomAttribute() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
