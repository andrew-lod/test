package apple.backend.ticketManagement.domain;

import apple.backend.filter.dto.FilterDto;
import apple.backend.filter.exceptions.FilterNotFoundException;
import apple.backend.filter.repository.FilterRepository;
import apple.backend.ticketManagement.api.dto.*;
import apple.backend.ticketManagement.api.exception.TicketNotFoundException;
import apple.backend.ticketManagement.domain.entity.keys.AttachmentId;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import apple.backend.filter.domain.entity.Filter;
import apple.backend.ticketManagement.api.ITicketService;
import apple.backend.ticketManagement.api.exception.ImportFileNoNewTicketsException;
import apple.backend.ticketManagement.repository.*;
import apple.backend.ticketManagement.domain.entity.*;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import apple.backend.ticketManagement.api.dto.PagedTicketsOverviewDto;
import apple.backend.ticketManagement.api.dto.TicketDto;
import apple.backend.ticketManagement.api.dto.TicketOverviewDto;
import apple.backend.ticketManagement.api.dto.AttachmentDto;
import apple.backend.ticketManagement.api.dto.CommentDto;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TicketService implements ITicketService {
    private ITicketMapperService ticketMapper;

    private TicketRepository ticketRepository;
    private IssueTypeRepository issueTypeRepository;
    private ResolutionRepository resolutionRepository;
    private StatusRepository statusRepository;
    private UserReferenceRepository userReferenceRepository;
    private ProjectRepository projectRepository;
    private LabelRepository labelRepository;
    private AttachmentRepository attachmentRepository;
    private CommentRepository commentRepository;
    private ArchivedTicketRepository archivedTicketRepository;
    private FilterRepository filterRepository;
    private CustomValueRespository customValueRepository;

    @Autowired
    public TicketService(
            ITicketMapperService ticketMapper,
            TicketRepository ticketRepository,
            IssueTypeRepository issueTypeRepository,
            ResolutionRepository resolutionRepository,
            StatusRepository statusRepository,
            UserReferenceRepository userReferenceRepository,
            ProjectRepository projectRepository,
            LabelRepository labelRepository,
            FilterRepository filterRepository,
            AttachmentRepository attachmentRepository,
            CommentRepository commentRepository,
            ArchivedTicketRepository archivedTicketRepository,
            CustomValueRespository customValueRepository
    ) {
        this.ticketMapper = ticketMapper;
        this.ticketRepository = ticketRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.resolutionRepository = resolutionRepository;
        this.statusRepository = statusRepository;
        this.userReferenceRepository = userReferenceRepository;
        this.projectRepository = projectRepository;
        this.labelRepository = labelRepository;
        this.filterRepository = filterRepository;
        this.attachmentRepository = attachmentRepository;
        this.commentRepository = commentRepository;
        this.archivedTicketRepository = archivedTicketRepository;
        this.customValueRepository = customValueRepository;
    }

    @Transactional
    public TicketDto getTicket(String id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(new TicketNotFoundException(id));
        Hibernate.initialize(ticket.getAttachments());
        Hibernate.initialize(ticket.getComments());

        return mapToTicketDto(ticket);
    }

    @Override
    public FiltersDto getTicketFilters() {
        List<String> projects = extract(projectRepository.findAll(), Project::getName);
        List<String> issueTypes = extract(issueTypeRepository.findAll(), IssueType::getName);
        List<String> resolutions = extract(resolutionRepository.findAll(), Resolution::getName);
        List<String> statuses = extract(statusRepository.findAll(), Status::getName);
        List<String> labels = extract(labelRepository.findAll(), Label::getName);
        List<String> users = extract(userReferenceRepository.findAll(), UserReference::getUsername);

        return new FiltersDto(projects, issueTypes, resolutions, statuses, labels, users);
    }

    public PagedTicketsOverviewDto getTicketsOverview(int page, String keywordsString, String issueType, String resolution, String status, String label) {
        Pageable pageable = PageRequest.of(page, 15);

        if(!keywordsString.isEmpty() || !issueType.isEmpty() || !resolution.isEmpty() || !status.isEmpty() || !label.isEmpty()) {
            List<String> keywords = List.of(keywordsString.split(",\\s*"));
            Specification<Ticket> spec = specificationForKeywords(keywords);
            spec = addSpecifications(spec, issueType, resolution, status, label);
            if(spec != null)
                return mapToTicketOverviewDto(ticketRepository.findAll(spec, pageable));
        }

        return mapToTicketOverviewDto(ticketRepository.findAll(pageable));
    }

    private PagedTicketsOverviewDto mapToTicketOverviewDto(Page<Ticket> pagedTicket) {
        return new PagedTicketsOverviewDto(
                pagedTicket.getContent().stream()
                        .map(ticket -> new TicketOverviewDto(ticket.getIssueKey(), ticket.getSummary()))
                        .toList(),
                pagedTicket.getNumber(),
                pagedTicket.getTotalPages(),
                pagedTicket.getTotalElements());
    }

    public Specification<Ticket> specificationForKeywords(List<String> keywords) {
        return keywords.stream()
                .map(this::buildKeywordSpecification)
                .reduce(Specification::and)
                .orElse(null);
    }

    private Specification<Ticket> buildKeywordSpecification(String keyword) {
        return (root, query, criteriaBuilder) -> {
            Predicate inIssueKey = criteriaBuilder.like(criteriaBuilder.lower(root.get("issueKey")),
                    "%" + keyword.toLowerCase() + "%");
            Predicate inSummary = criteriaBuilder.like(criteriaBuilder.lower(root.get("summary")),
                    "%" + keyword.toLowerCase() + "%");
            Predicate inDescription = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                    "%" + keyword.toLowerCase() + "%");

            return criteriaBuilder.or(inIssueKey, inSummary, inDescription);
        };
    }

    public Specification<Ticket> addSpecifications(Specification<Ticket> spec, String issueType, String resolution, String status, String label) {
        Map<String, String> attMap = new HashMap<>();
        attMap.put("issueType", issueType);
        attMap.put("resolution", resolution);
        attMap.put("status", status);
        attMap.put("label", label);

        for (Map.Entry<String, String> entry : attMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isEmpty()) {
                spec = spec.and(specificationBuilder(key, value));
            }
        }

        return spec;
    }

    public Specification<Ticket> specificationBuilder(String attribute, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value != null && !value.isEmpty()) {
                return criteriaBuilder.equal(criteriaBuilder.lower(root.get(attribute)), value.toLowerCase());
            }
            return criteriaBuilder.conjunction();
        };
    }

    public TicketDto mapToTicketDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();

        ticketDto.setIssueKey(ticket.getIssueKey());
        ticketDto.setSummary(ticket.getSummary());
        ticketDto.setDescription(ticket.getDescription());

        ticketDto.setCreatedAt(ticket.getCreatedAt());
        ticketDto.setResolvedAt(ticket.getResolvedAt());

        if (ticket.getIssueType() != null)
            ticketDto.setIssueType(ticket.getIssueType().getName());
        if (ticket.getResolution() != null)
            ticketDto.setResolution(ticket.getResolution().getName());
        if (ticket.getStatus() != null)
            ticketDto.setStatus(ticket.getStatus().getName());

        if (ticket.getCreator() != null)
            ticketDto.setCreator(ticket.getCreator().getUsername());
        if (ticket.getReporter() != null)
            ticketDto.setReporter(ticket.getReporter().getUsername());
        if (ticket.getAssignee() != null)
            ticketDto.setAssignee(ticket.getAssignee().getUsername());

        if (ticket.getProject() != null)
            ticketDto.setProject(ticket.getProject().getName());

        ticketDto.setWatchers(ticket.getWatchers().stream()
                .map(UserReference::getUsername)
                .toList());
        ticketDto.setLabels(ticket.getLabels().stream()
                .map(Label::getName)
                .toList());
        ticketDto.setComments(ticket.getComments().stream()
                .map(comment -> new CommentDto(
                        comment.getCreatedAt(),
                        comment.getAuthor().getUsername(),
                        comment.getBody()))
                .toList());
        ticketDto.setAttachments(ticket.getAttachments().stream()
                .map(attachment -> new AttachmentDto(
                        attachment.getUploadedAt(),
                        attachment.getUploader().getUsername(),
                        attachment.getName(),
                        attachment.getUrl()))
                .toList());

        ticketDto.setArchivedTicketId(String.valueOf(ticket.getArchivedTicket().getId()));
        ticketDto.setCustomValues(ticket.getCustomValues().stream()
                .collect(Collectors.toMap(customValue -> customValue.getCustomAttribute().getKey(), CustomValue::getValue)));

        return ticketDto;
    }

    @Override
    @Transactional
    public void importTickets(MultipartFile file) {
        List<Ticket> newTickets = ticketMapper.createTickets(file);

        // Filter out tickets that already exist in the database
        newTickets = newTickets.stream()
                .filter(ticket -> ticketRepository.findById(ticket.getIssueKey()).isEmpty())
                .toList();

        // If there are no new tickets, throw an exception
        if (newTickets.isEmpty()) {
            throw new ImportFileNoNewTicketsException();
        }

        // Save the new tickets to the database
        newTickets.forEach(ticket -> archivedTicketRepository.save(ticket.getArchivedTicket()));

        saveTickets(newTickets);
    }

    @Override
    @Transactional
    public void refreshTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        attachmentRepository.deleteAllInBatch();
        commentRepository.deleteAllInBatch();
        customValueRepository.deleteAllInBatch();
        ticketMapper.populateTickets(tickets);

        saveTickets(tickets);
    }

    private void saveTickets(List<Ticket> tickets) {
        List<IssueType> uniqueIssueTypes = toUnique(extract(tickets, Ticket::getIssueType), IssueType::getName);
        List<Resolution> uniqueResolutions = toUnique(extract(tickets, Ticket::getResolution), Resolution::getName);
        List<Status> uniqueStatuses = toUnique(extract(tickets, Ticket::getStatus), Status::getName);

        List<UserReference> uniqueUsers = getUniqueUsers(tickets);
        List<Project> projects = extract(tickets, Ticket::getProject);
        List<Label> uniqueLabels = toUnique(extractFromList(tickets, Ticket::getLabels), Label::getName);

        List<CustomValue> customValues = tickets.stream()
                .map(Ticket::getCustomValues)
                .flatMap(List::stream)
                .toList();

        issueTypeRepository.saveAll(uniqueIssueTypes);
        resolutionRepository.saveAll(uniqueResolutions);
        statusRepository.saveAll(uniqueStatuses);

        labelRepository.saveAll(uniqueLabels);

        userReferenceRepository.saveAll(uniqueUsers);
        projectRepository.saveAll(projects);

        // Save each ticket individually to avoid issues with clearing collections
        tickets.forEach(ticket -> {
            // Detach the ticket to prevent Hibernate from tracking changes to the collections
            ticketRepository.detach(ticket, ticket.getIssueKey());

            // Ensure all associated collections are initialized and managed properly
            if(ticket.getWatchers() != null)
                ticket.setWatchers(new ArrayList<>(ticket.getWatchers()));
            if(ticket.getLabels() != null)
                ticket.setLabels(new ArrayList<>(ticket.getLabels()));
            if(ticket.getComments() != null)
                ticket.setComments(new ArrayList<>(ticket.getComments()));
            if(ticket.getAttachments() != null)
                ticket.setAttachments(new ArrayList<>(ticket.getAttachments()));

            // Save the ticket
            ticketRepository.save(ticket);
        });

        customValueRepository.saveAll(customValues);
    }

    private List<UserReference> getUniqueUsers(List<Ticket> tickets) {
        List<UserReference> uniqueUsers = new ArrayList<>();

        uniqueUsers.addAll(extract(tickets, Ticket::getCreator));
        uniqueUsers.addAll(extract(tickets, Ticket::getReporter));
        uniqueUsers.addAll(extract(tickets, Ticket::getAssignee));
        uniqueUsers.addAll(extractFromList(tickets, Ticket::getWatchers));
        uniqueUsers.addAll(extract(tickets, ticket -> ticket.getProject().getLead()));
        uniqueUsers.addAll(extractFromList(tickets, Ticket::getComments, Comment::getAuthor));
        uniqueUsers.addAll(extractFromList(tickets, Ticket::getAttachments, Attachment::getUploader));

        uniqueUsers = toUnique(uniqueUsers, UserReference::getUsername);

        return uniqueUsers;
    }

    private <T, R> List<R> extract(List<T> list, Function<T, R> extractor) {
        return list.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .toList();
    }

    private <T, R> List<R> extractFromList(List<T> list, Function<T, List<R>> extractor) {
        return list.stream()
                .map(extractor)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .toList();
    }

    private <T, M, R> List<R> extractFromList(List<T> list, Function<T, List<M>> listExtractor,
            Function<M, R> extractor) {
        return extract(extractFromList(list, listExtractor), extractor);
    }

    private <T, R> List<T> toUnique(List<T> list, Function<T, R> extractor) {
        return new ArrayList<>(list.stream()
                .collect(Collectors.toMap(
                        extractor,
                        Function.identity(),
                        (existing, replacement) -> existing))
                .values());
    }


    public void saveFilter(String name, String filterQuery) {
        Filter filter = new Filter(name, filterQuery);
        filterRepository.save(filter);
    }

    public List<FilterDto> getAllFilters() {
        return filterRepository.findAll().stream()
                .map(filter -> new FilterDto(filter.getName(), filter.getFilter()))
                .collect(Collectors.toList());
    }

    public void deleteFilter(Integer id) {
        Filter filter = filterRepository.findById(id)
                .orElseThrow(() -> new FilterNotFoundException(id));
        filterRepository.delete(filter);
    }
}
