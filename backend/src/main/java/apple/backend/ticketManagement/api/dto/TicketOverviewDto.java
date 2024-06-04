package apple.backend.ticketManagement.api.dto;

public class TicketOverviewDto {
    private String issueKey;
    private String summary;

    public TicketOverviewDto(String issueKey, String summary) {
        this.issueKey = issueKey;
        this.summary = summary;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
