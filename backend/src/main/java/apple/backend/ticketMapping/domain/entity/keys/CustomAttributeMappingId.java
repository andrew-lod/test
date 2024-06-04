package apple.backend.ticketMapping.domain.entity.keys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CustomAttributeMappingId implements Serializable {
    private long route;
    private String customAttribute;

    public CustomAttributeMappingId() {
    }

    public CustomAttributeMappingId(long route, String customAttribute) {
        this.route = route;
        this.customAttribute = customAttribute;
    }

    public String getCustomAttribute() {
        return customAttribute;
    }

    public void setCustomAttribute(String customAttribute) {
        this.customAttribute = customAttribute;
    }

    public long getRoute() {
        return route;
    }

    public void setRoute(long route) {
        this.route = route;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CustomAttributeMappingId that = (CustomAttributeMappingId) o;

        if (route != that.route)
            return false;
        return customAttribute != null ? customAttribute.equals(that.customAttribute) : that.customAttribute == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (route ^ (route >>> 32));
        result = 31 * result + (customAttribute != null ? customAttribute.hashCode() : 0);
        return result;
    }
}
