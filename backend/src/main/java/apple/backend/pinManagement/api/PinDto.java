package apple.backend.pinManagement.api;

public class PinDto {
    private String issueKey;

    public PinDto(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getIssueKey() {
        return issueKey;
    }
}
