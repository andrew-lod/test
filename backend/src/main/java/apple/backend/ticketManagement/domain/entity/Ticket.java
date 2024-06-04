package apple.backend.ticketManagement.domain.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    private String issueKey;
    private String summary;
    private String description;

    @Column(name = "created_date")
    private Date createdAt;
    @Column(name = "resolved_date")
    private Date resolvedAt;

    @ManyToOne
    @JoinColumn(name = "issue_type")
    private IssueType issueType;
    @ManyToOne
    @JoinColumn(name = "resolution")
    private Resolution resolution;
    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "project_key")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "creator")
    private UserReference creator;
    @ManyToOne
    @JoinColumn(name = "reporter")
    private UserReference reporter;
    @ManyToOne
    @JoinColumn(name = "assignee")
    private UserReference assignee;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "watchers_of_ticket",
            joinColumns = @JoinColumn(name = "issue_key"),
            inverseJoinColumns = @JoinColumn(name = "username")
    )
    private List<UserReference> watchers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "labels_in_ticket",
            joinColumns = @JoinColumn(name = "issue_key"),
            inverseJoinColumns = @JoinColumn(name = "name")
    )
    private List<Label> labels;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Comment> comments;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Attachment> attachments;

    @ManyToOne
    @JoinColumn(name = "object_id")
    private ArchivedTicket archivedTicket;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<CustomValue> customValues;

    public Ticket() {
    }

    public Ticket(String issueKey, String summary, String description, Date createdAt, Date resolvedAt, IssueType issueType, Resolution resolution, Status status, Project project, UserReference creator, UserReference reporter, UserReference assignee, List<UserReference> watchers, List<Label> labels, List<Comment> comments, List<Attachment> attachments) {
        this.issueKey = issueKey;
        this.summary = summary;
        this.description = description;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
        this.issueType = issueType;
        this.resolution = resolution;
        this.status = status;
        this.project = project;
        this.creator = creator;
        this.reporter = reporter;
        this.assignee = assignee;
        this.watchers = watchers;
        this.labels = labels;
        this.comments = comments;
        this.attachments = attachments;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public UserReference getCreator() {
        return creator;
    }

    public void setCreator(UserReference creator) {
        this.creator = creator;
    }

    public UserReference getReporter() {
        return reporter;
    }

    public void setReporter(UserReference reporter) {
        this.reporter = reporter;
    }

    public UserReference getAssignee() {
        return assignee;
    }

    public void setAssignee(UserReference assignee) {
        this.assignee = assignee;
    }

    public List<UserReference> getWatchers() {
        return watchers;
    }

    public void setWatchers(List<UserReference> watchers) {
        this.watchers = watchers;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public ArchivedTicket getArchivedTicket() {
        return archivedTicket;
    }

    public void setArchivedTicket(ArchivedTicket archivedTicket) {
        this.archivedTicket = archivedTicket;
    }


    public List<CustomValue> getCustomValues() {
        return customValues;
    }

    public void setCustomValues(List<CustomValue> customValues) {
        this.customValues = customValues;
    }
}
