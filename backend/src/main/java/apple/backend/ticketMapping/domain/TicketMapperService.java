package apple.backend.ticketMapping.domain;

import apple.backend.ticketManagement.api.exception.ImportFileUnsupportedFormatException;
import apple.backend.ticketManagement.domain.ITicketMapperService;
import apple.backend.ticketManagement.domain.entity.*;
import apple.backend.ticketManagement.domain.entity.ArchivedTicket;
import apple.backend.ticketMapping.repository.ArchivedTicketMappingRepository;
import apple.backend.ticketMapping.repository.CustomAttributeMappingRepository;
import apple.backend.ticketMapping.repository.GlobalMappingRepository;
import apple.backend.ticketMapping.domain.entity.ArchivedTicketMapping;
import apple.backend.ticketMapping.domain.entity.Route;
import apple.backend.ticketMapping.domain.entity.GlobalMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.MissingRequiredPropertiesException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class TicketMapperService implements ITicketMapperService {
    public static final String TICKET_ISSUE_KEY = "ticket_issue_key";
    private static final String TICKET_SUMMARY = "ticket_summary";
    private static final String TICKET_DESCRIPTION = "ticket_description";

    private static final String TICKET_CREATED_AT = "ticket_created_at";
    private static final String TICKET_RESOLVED_AT = "ticket_resolved_at";

    private static final String TICKET_ISSUE_TYPE = "ticket_issue_type";
    private static final String TICKET_RESOLUTION = "ticket_resolution";
    private static final String TICKET_STATUS = "ticket_status";

    private static final String TICKET_CREATOR = "ticket_creator";
    private static final String TICKET_REPORTER = "ticket_reporter";
    private static final String TICKET_ASSIGNEE = "ticket_assignee";

    public static final String TICKET_WATCHERS = "ticket_watchers";
    private static final String WATCHER_NAME = "watcher_name";

    public static final String TICKET_LABELS = "ticket_labels";
    private static final String LABEL_NAME = "label_name";

    public static final String TICKET_COMMENTS = "ticket_comments";
    private static final String COMMENT_AUTHOR = "comment_author";
    private static final String COMMENT_BODY = "comment_body";
    private static final String COMMENT_CREATED_AT = "comment_created_at";

    public static final String TICKET_ATTACHMENTS = "ticket_attachments";
    private static final String ATTACHMENT_NAME = "attachment_name";
    private static final String ATTACHMENT_URL = "attachment_url";
    private static final String ATTACHMENT_UPLOADER = "attachment_uploader";
    private static final String ATTACHMENT_CREATED_AT = "attachment_uploaded_at";

    public static final String PROJECT_KEY = "project_key";
    public static final String PROJECT_NAME = "project_name";
    private static final String PROJECT_LEAD = "project_lead";
    private static final String PROJECT_TYPE = "project_type";
    private static final String PROJECT_DESCRIPTION = "project_description";
    private static final String PROJECT_URL = "project_url";

    private GlobalMappingRepository globalMappingRepository;
    private ArchivedTicketMappingRepository archivedTicketMappingRepository;
    private CustomAttributeMappingRepository customAttributeMappingRepository;
    private List<IObjectParser> parsers;

    @Autowired
    public TicketMapperService(
            GlobalMappingRepository globalMappingRepository,
            ArchivedTicketMappingRepository archivedTicketMappingRepository,
            CustomAttributeMappingRepository customAttributeMappingRepository,
            List<IObjectParser> parsers) {
        this.globalMappingRepository = globalMappingRepository;
        this.archivedTicketMappingRepository = archivedTicketMappingRepository;
        this.customAttributeMappingRepository = customAttributeMappingRepository;
        this.parsers = parsers;
    }

    @Override
    public List<Ticket> createTickets(MultipartFile file) {
        // Get the supported parser for the file
        String contentType = file.getContentType();
        IObjectParser parser = getSupportedParser(contentType);

        // Get a map of global mappings for every ticket
        Map<String, Route> globalMappings = globalMappingRepository.findAll().stream()
                .collect(Collectors.toMap(GlobalMapping::getKey, GlobalMapping::getRoute));

        // Use the parser to get a list of objects from the file and map to tickets
        return parser.objectsAsString(file).stream()
                .map(object -> {
                    Ticket ticket = new Ticket();
                    ArchivedTicket archivedTicket = new ArchivedTicket(object, contentType);
                    ticket.setArchivedTicket(archivedTicket);
                    populateTicket(ticket, globalMappings, Map.of(), parser);

                    return ticket;
                })
                .toList();
    }

    @Override
    public void populateTickets(List<Ticket> tickets) {
        // Get a map of global mappings for every ticket
        Map<String, Route> globalMappings = globalMappingRepository.findAll().stream()
                .collect(Collectors.toMap(GlobalMapping::getKey, GlobalMapping::getRoute));

        tickets.forEach(ticket -> {
            // Get a map of ticket mappings for this archived ticket
            ArchivedTicketMapping probe = new ArchivedTicketMapping(ticket.getArchivedTicket());
            Map<String, Route> ticketMappings = archivedTicketMappingRepository.findAll(Example.of(probe)).stream()
                    .collect(Collectors.toMap(ArchivedTicketMapping::getKey, ArchivedTicketMapping::getRoute));

            // Get the parser for this archived ticket
            IObjectParser parser = getSupportedParser(ticket.getArchivedTicket().getMediaType());

            populateTicket(ticket, globalMappings, ticketMappings, parser);
        });
    }

    private IObjectParser getSupportedParser(String contentType) {
        return parsers.stream()
                .filter(p -> p.supports(contentType))
                .findFirst()
                .orElseThrow(() -> new ImportFileUnsupportedFormatException("Media type is not supported"));
    }

    private void populateTicket(Ticket ticket, Map<String, Route> globalMappings, Map<String, Route> ticketMappings,
            IObjectParser parser) {
        String ticketObject = ticket.getArchivedTicket().getTicketObject();

        Predicate<String> stringNotNullOrBlank = string -> (string != null && !string.isBlank());
        Predicate<List<?>> listNotNullOrEmpty = list -> (list != null && !list.isEmpty());


        Route issueKeyRoute = getRouteByKey(TICKET_ISSUE_KEY, globalMappings, ticketMappings);
        String issueKey = parser.getValue(ticketObject, issueKeyRoute);
        setOrThrow(ticket::setIssueKey, issueKey, stringNotNullOrBlank, MissingRequiredPropertiesException::new);


        Route summaryRoute = getRouteByKey(TICKET_SUMMARY, globalMappings, ticketMappings);
        String summary = parser.getValue(ticketObject, summaryRoute);
        set(ticket::setSummary, summary, stringNotNullOrBlank);


        Route descriptionRoute = getRouteByKey(TICKET_DESCRIPTION, globalMappings, ticketMappings);
        String description = parser.getValue(ticketObject, descriptionRoute);
        set(ticket::setDescription, description, stringNotNullOrBlank);


        Route createdAtRoute = getRouteByKey(TICKET_CREATED_AT, globalMappings, ticketMappings);
        Date createdAt = parser.getDate(ticketObject, createdAtRoute);
        set(ticket::setCreatedAt, createdAt, Objects::nonNull);


        Route resolvedAtRoute = getRouteByKey(TICKET_RESOLVED_AT, globalMappings, ticketMappings);
        Date resolvedAt = parser.getDate(ticketObject, resolvedAtRoute);
        set(ticket::setResolvedAt, resolvedAt, Objects::nonNull);


        Route issueTypeRoute = getRouteByKey(TICKET_ISSUE_TYPE, globalMappings, ticketMappings);
        String issueType = parser.getValue(ticketObject, issueTypeRoute);
        set(t -> ticket.setIssueType(new IssueType(t)), issueType, stringNotNullOrBlank);


        Route resolutionRoute = getRouteByKey(TICKET_RESOLUTION, globalMappings, ticketMappings);
        String resolution = parser.getValue(ticketObject, resolutionRoute);
        set(r -> ticket.setResolution(new Resolution(r)), resolution, stringNotNullOrBlank);


        Route statusRoute = getRouteByKey(TICKET_STATUS, globalMappings, ticketMappings);
        String status = parser.getValue(ticketObject, statusRoute);
        set(s -> ticket.setStatus(new Status(s)), status, stringNotNullOrBlank);


        Route creatorRoute = getRouteByKey(TICKET_CREATOR, globalMappings, ticketMappings);
        String creator = parser.getValue(ticketObject, creatorRoute);
        set(c -> ticket.setCreator(new UserReference(c)), creator, stringNotNullOrBlank);


        Route reporterRoute = getRouteByKey(TICKET_REPORTER, globalMappings, ticketMappings);
        String reporter = parser.getValue(ticketObject, reporterRoute);
        set(r -> ticket.setReporter(new UserReference(r)), reporter, stringNotNullOrBlank);


        Route assigneeRoute = getRouteByKey(TICKET_ASSIGNEE, globalMappings, ticketMappings);
        String assignee = parser.getValue(ticketObject, assigneeRoute);
        set(a -> ticket.setAssignee(new UserReference(a)), assignee, stringNotNullOrBlank);


        Route projectKeyRoute = getRouteByKey(PROJECT_KEY, globalMappings, ticketMappings);
        String projectKey = parser.getValue(ticketObject, projectKeyRoute);

        if (stringNotNullOrBlank.test(projectKey)) {
            Project project = new Project();
            project.setKey(projectKey);

            Route projectNameRoute = getRouteByKey(PROJECT_NAME, globalMappings, ticketMappings);
            Route projectLeadRoute = getRouteByKey(PROJECT_LEAD, globalMappings, ticketMappings);
            Route projectTypeRoute = getRouteByKey(PROJECT_TYPE, globalMappings, ticketMappings);
            Route projectDescriptionRoute = getRouteByKey(PROJECT_DESCRIPTION, globalMappings, ticketMappings);
            Route projectUrlRoute = getRouteByKey(PROJECT_URL, globalMappings, ticketMappings);

            String projectName = parser.getValue(ticketObject, projectNameRoute);
            String projectLead = parser.getValue(ticketObject, projectLeadRoute);
            String projectType = parser.getValue(ticketObject, projectTypeRoute);
            String projectDescription = parser.getValue(ticketObject, projectDescriptionRoute);
            String projectUrl = parser.getValue(ticketObject, projectUrlRoute);

            set(project::setName, projectName, stringNotNullOrBlank);
            set(l -> project.setLead(new UserReference(l)), projectLead, stringNotNullOrBlank);
            set(project::setType, projectType, stringNotNullOrBlank);
            set(project::setDescription, projectDescription, stringNotNullOrBlank);
            set(project::setUrl, projectUrl, stringNotNullOrBlank);

            ticket.setProject(project);
        }


        Route watchersRoute = getRouteByKey(TICKET_WATCHERS, globalMappings, ticketMappings);
        List<String> watcherObjects = parser.getArray(ticketObject, watchersRoute);

        if (listNotNullOrEmpty.test(watcherObjects)) {
            Route watcherNameRoute = getRouteByKey(WATCHER_NAME, globalMappings, ticketMappings);
            List<UserReference> watchers = watcherObjects.stream()
                    .map(watcherObject -> {
                        String val = parser.getValue(watcherObject, watcherNameRoute);
                        return val;
                    })
                    .filter(stringNotNullOrBlank)
                    .map(UserReference::new)
                    .toList();

            set(ticket::setWatchers, watchers, list -> !list.isEmpty());
        }


        Route labelsRoute = getRouteByKey(TICKET_LABELS, globalMappings, ticketMappings);
        List<String> labelObjects = parser.getArray(ticketObject, labelsRoute);

        if (listNotNullOrEmpty.test(labelObjects)) {
            Route labelNameRoute = getRouteByKey(LABEL_NAME, globalMappings, ticketMappings);
            List<Label> labels = labelObjects.stream()
                    .map(labelObject -> parser.getValue(labelObject, labelNameRoute))
                    .filter(stringNotNullOrBlank)
                    .map(Label::new)
                    .toList();

            set(ticket::setLabels, labels, list -> !list.isEmpty());
        }


        Route commentsRoute = getRouteByKey(TICKET_COMMENTS, globalMappings, ticketMappings);
        List<String> commentObjects = parser.getArray(ticketObject, commentsRoute);

        if (listNotNullOrEmpty.test(commentObjects)) {
            Route commentAuthorRoute = getRouteByKey(COMMENT_AUTHOR, globalMappings, ticketMappings);
            Route commentBodyRoute = getRouteByKey(COMMENT_BODY, globalMappings, ticketMappings);
            Route commentCreatedAtRoute = getRouteByKey(COMMENT_CREATED_AT, globalMappings, ticketMappings);

            List<Comment> comments = commentObjects.stream()
                    .map(commentObject -> {
                        String author = parser.getValue(commentObject, commentAuthorRoute);
                        String body = parser.getValue(commentObject, commentBodyRoute);
                        Date commentCreatedAt = parser.getDate(commentObject, commentCreatedAtRoute);

                        Comment comment = new Comment();
                        set(comment::setBody, body, stringNotNullOrBlank);
                        if (comment.getBody() == null)
                            return null;

                        set(a -> comment.setAuthor(new UserReference(a)), author, stringNotNullOrBlank);
                        set(comment::setCreatedAt, commentCreatedAt, Objects::nonNull);
                        set(comment::setTicket, ticket, Objects::nonNull);

                        return comment;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            set(ticket::setComments, comments, list -> !list.isEmpty());
        }


        Route attachmentsRoute = getRouteByKey(TICKET_ATTACHMENTS, globalMappings, ticketMappings);
        List<String> attachmentObjects = parser.getArray(ticketObject, attachmentsRoute);

        if (listNotNullOrEmpty.test(attachmentObjects)) {
            Route attachmentNameRoute = getRouteByKey(ATTACHMENT_NAME, globalMappings, ticketMappings);
            Route attachmentUrlRoute = getRouteByKey(ATTACHMENT_URL, globalMappings, ticketMappings);
            Route attachmentUploaderRoute = getRouteByKey(ATTACHMENT_UPLOADER, globalMappings, ticketMappings);
            Route attachmentCreatedAtRoute = getRouteByKey(ATTACHMENT_CREATED_AT, globalMappings, ticketMappings);

            List<Attachment> attachments = attachmentObjects.stream()
                    .map(attachmentObject -> {
                        String name = parser.getValue(attachmentObject, attachmentNameRoute);
                        String url = parser.getValue(attachmentObject, attachmentUrlRoute);
                        String uploader = parser.getValue(attachmentObject, attachmentUploaderRoute);
                        Date attachmentCreatedAt = parser.getDate(attachmentObject, attachmentCreatedAtRoute);

                        Attachment attachment = new Attachment();
                        set(attachment::setName, name, stringNotNullOrBlank);
                        set(attachment::setUploadedAt, attachmentCreatedAt, Objects::nonNull);
                        if (attachment.getName() == null || attachment.getUploadedAt() == null)
                            return null;

                        set(attachment::setUrl, url, stringNotNullOrBlank);
                        set(u -> attachment.setUploader(new UserReference(u)), uploader, stringNotNullOrBlank);
                        set(attachment::setTicket, ticket, Objects::nonNull);

                        return attachment;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            set(ticket::setAttachments, attachments, list -> !list.isEmpty());

            List<CustomValue> customValues = new ArrayList<>();

            customAttributeMappingRepository.findAll().forEach(mapping -> {
                CustomAttribute customAttribute = mapping.getCustomAttribute();
                Route customAttributeRoute = mapping.getRoute();
                String customAttributeValue = parser.getValue(ticketObject, customAttributeRoute);
                if (stringNotNullOrBlank.test(customAttributeValue)) {
                    CustomValue customValue = new CustomValue();
                    customValue.setTicket(ticket);
                    customValue.setCustomAttribute(customAttribute);
                    customValue.setValue(customAttributeValue);
                    customValues.add(customValue);
                }
            });
            ticket.setCustomValues(customValues);
        }
    }

    private <T> void set(Consumer<T> setter, T checkValue, Predicate<T> check) {
        if (check.test(checkValue)) {
            setter.accept(checkValue);
        }
    }

    private Route getRouteByKey(String key, Map<String, Route> globalMappings, Map<String, Route> ticketMappings) {
        return ticketMappings.containsKey(key) ? ticketMappings.get(key) : globalMappings.get(key);
    }

    private <X extends Throwable, T> void setOrThrow(Consumer<T> setter, T checkValue, Predicate<T> check,
            Supplier<? extends X> exceptionSupplier) throws X {
        if (check.test(checkValue)) {
            setter.accept(checkValue);
        } else {
            throw exceptionSupplier.get();
        }
    }
}
