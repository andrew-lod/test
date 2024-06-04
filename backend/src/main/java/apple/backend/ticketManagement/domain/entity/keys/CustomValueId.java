package apple.backend.ticketManagement.domain.entity.keys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CustomValueId implements Serializable {
    private String ticket;
    private String customAttribute;
    private String value;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getCustomAttribute() {
        return customAttribute;
    }

    public void setCustomAttribute(String customAttribute) {
        this.customAttribute = customAttribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomValueId that = (CustomValueId) o;

        if (ticket != null ? !ticket.equals(that.ticket) : that.ticket != null) return false;
        if (customAttribute != null ? !customAttribute.equals(that.customAttribute) : that.customAttribute != null)
            return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = ticket != null ? ticket.hashCode() : 0;
        result = 31 * result + (customAttribute != null ? customAttribute.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
