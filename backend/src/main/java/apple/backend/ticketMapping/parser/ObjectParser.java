package apple.backend.ticketMapping.parser;

import apple.backend.ticketMapping.domain.IObjectParser;

public abstract class ObjectParser implements IObjectParser {
    private final String contentType;

    /**
     * @param contentType The content type that this parser supports
     */
    public ObjectParser(String contentType) {
        this.contentType = contentType;
    }

    public boolean supports(String contentType) {
        return this.contentType.equals(contentType);
    }
}
