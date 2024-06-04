package apple.backend.ticketMapping.domain.entity;

import apple.backend.ticketManagement.domain.entity.CustomAttribute;
import apple.backend.ticketMapping.domain.entity.keys.CustomAttributeMappingId;
import jakarta.persistence.*;

@Entity
@Table(name = "customattributemapping")
@IdClass(CustomAttributeMappingId.class)
public class CustomAttributeMapping {
    @Id
    @ManyToOne
    @JoinColumn(name = "mapping_id")
    private Route route;
    @Id
    @ManyToOne
    @JoinColumn(name = "custom_attribute_key")
    private CustomAttribute customAttribute;

    public CustomAttributeMapping() {
    }

    public CustomAttributeMapping(Route route, CustomAttribute customAttribute) {
        this.route = route;
        this.customAttribute = customAttribute;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public CustomAttribute getCustomAttribute() {
        return customAttribute;
    }

    public void setCustomAttribute(CustomAttribute customAttribute) {
        this.customAttribute = customAttribute;
    }
}
