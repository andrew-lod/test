package apple.backend.ticketMapping.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "globalmapping")
public class GlobalMapping {
    @Id
    @Column(name = "\"key\"")
    private String key;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    public GlobalMapping() {
    }

public GlobalMapping(String key, Route route) {
        this.key = key;
        this.route = route;
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
