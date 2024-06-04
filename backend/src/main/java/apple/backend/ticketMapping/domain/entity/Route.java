package apple.backend.ticketMapping.domain.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_route")
    private Route childRoute;

    @Column(name = "route")
    private String path;

    @Column(name = "key_processing")
    private String pathPattern;

    @Column(name = "value_processing")
    private String processingRule;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchivedTicketMapping> archivedObjectMappings;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GlobalMapping> globalMappings;

    public Route() {}
    public Route(String path) {
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Route getChildRoute() {
        return childRoute;
    }

    public void setChildRoute(Route childRoute) {
        this.childRoute = childRoute;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String key) {
        this.path = key;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    public void setPathPattern(String keyProcessing) {
        this.pathPattern = keyProcessing;
    }

    public String getProcessingRule() {
        return processingRule;
    }

    public void setProcessingRule(String valueProcessing) {
        this.processingRule = valueProcessing;
    }
}
