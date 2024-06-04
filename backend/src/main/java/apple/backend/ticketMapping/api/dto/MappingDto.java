package apple.backend.ticketMapping.api.dto;

public class MappingDto {
    private String keyName;
    private RouteDto route;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public RouteDto getRoute() {
        return route;
    }

    public void setRoute(RouteDto route) {
        this.route = route;
    }
}
