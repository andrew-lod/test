package apple.backend.ticketMapping.api.dto;

public class RouteDto {
    private String path;
    private RouteDto next;
    private String keyProcessing;
    private String valueProcessing;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RouteDto getNext() {
        return next;
    }

    public void setNext(RouteDto next) {
        this.next = next;
    }

    public String getKeyProcessing() {
        return keyProcessing;
    }

    public void setKeyProcessing(String keyProcessing) {
        this.keyProcessing = keyProcessing;
    }

    public String getValueProcessing() {
        return valueProcessing;
    }

    public void setValueProcessing(String valueProcessing) {
        this.valueProcessing = valueProcessing;
    }
}
