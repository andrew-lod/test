package apple.backend.ticketManagement.api.dto;

import java.util.List;

public class FiltersDto {
    private List<String> projects;
    private List<String> issueTypes;
    private List<String> resolutions;
    private List<String> statuses;
    private List<String> labels;
    private List<String> users;

    public FiltersDto(List<String> projects, List<String> issueTypes, List<String> resolutions, List<String> statuses, List<String> labels, List<String> users) {
        this.projects = projects;
        this.issueTypes = issueTypes;
        this.resolutions = resolutions;
        this.statuses = statuses;
        this.labels = labels;
        this.users = users;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public List<String> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<String> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public List<String> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<String> resolutions) {
        this.resolutions = resolutions;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
