package apple.backend.ticketManagement;

import apple.backend.ticketManagement.api.exception.ImportFileNoNewTicketsException;
import apple.backend.ticketManagement.domain.entity.*;
import apple.backend.ticketManagement.repository.*;
import apple.backend.ticketManagement.domain.TicketService;
import apple.backend.ticketManagement.domain.ITicketMapperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.MockitoAnnotations.openMocks;

class TicketServiceTest {
    @Mock
    private ITicketMapperService mockedArchiveService;
    @Mock
    private TicketRepository mockedTicketRepository;
    @Mock
    private IssueTypeRepository mockedIssueTypeRepository;
    @Mock
    private ResolutionRepository mockedResolutionRepository;
    @Mock
    private StatusRepository mockedStatusRepository;
    @Mock
    private UserReferenceRepository mockedUserReferenceRepository;
    @Mock
    private ProjectRepository mockedProjectRepository;
    @Mock
    private LabelRepository mockedLabelRepository;

    @InjectMocks
    private TicketService sut;

    private IssueType issueType;
    private Resolution resolution;
    private Status status;
    private Label label1, label2;
    private UserReference user1, user2, user3, user4, user5;
    private Project project;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        openMocks(this);

        issueType = new IssueType();
        issueType.setName("Bug");

        resolution = new Resolution();
        resolution.setName("Fixed");

        status = new Status();
        status.setName("Open");

        label1 = new Label();
        label1.setName("Backend");
        label2 = new Label("Frontend");

        user1 = new UserReference();
        user1.setUsername("Andrew Lodeizen");
        user2 = new UserReference("Kyan Kersten");
        user3 = new UserReference("Haroon Muhammad");
        user4 = new UserReference("Nusayba El Mourabet");
        user5 = new UserReference("Matthijs Pieterse");

        project = new Project();
        project.setName("Ticket Vault");
        project.setType("Application");
        project.setDescription("Store all tickets in one place");
        project.setUrl("http://ticket-vault.com");
        project.setLead(user1);

        ticket = new Ticket();
        ticket.setIssueKey("EA-10");
        ticket.setSummary("Gaat iets verkeerd!");
        ticket.setDescription("Er gaat iets verkeerd als ik op de knop druk");
        ticket.setCreatedAt(new Date());
        ticket.setResolvedAt(new Date());
        ticket.setIssueType(issueType);
        ticket.setResolution(resolution);
        ticket.setStatus(status);
        ticket.setProject(project);
        ticket.setCreator(user1);
        ticket.setReporter(user2);
        ticket.setAssignee(user3);
        ticket.setWatchers(List.of(user4, user5));
        ticket.setLabels(List.of(label1, label2));

        Comment ticket1Comment1 = new Comment();
        ticket1Comment1.setTicket(ticket);
        ticket1Comment1.setBody("Dit is een comment");
        ticket1Comment1.setAuthor(user1);
        ticket1Comment1.setCreatedAt(new Date());
        Comment ticket1Comment2 = new Comment("Dit is een andere comment", user2, new Date());
        ticket1Comment2.setTicket(ticket);
        ticket.setComments(List.of(ticket1Comment1, ticket1Comment2));

        Attachment ticket1Attachment1 = new Attachment();
        ticket1Attachment1.setTicket(ticket);
        ticket1Attachment1.setName("attachment1.txt");
        ticket1Attachment1.setUploadedAt(new Date());
        ticket1Attachment1.setUploader(user1);
        ticket1Attachment1.setUrl("http://attachment1.com");
        Attachment ticket1Attachment2 = new Attachment("attachment2.txt", new Date(), user2, "http://attachment2.com");
        ticket1Attachment2.setTicket(ticket);
    }

    @Nested
    @DisplayName("importTickets()")
    class ImportTickets {
        private MultipartFile mockedFile;
        private Ticket duplicateTicket;

        @BeforeEach
        void setUp() {
            mockedFile = mock(MultipartFile.class);

            duplicateTicket = new Ticket();
            duplicateTicket.setIssueKey("EA-9");

            List<Ticket> returnedTickets = new ArrayList<>(List.of(ticket, duplicateTicket));
            when(mockedArchiveService.createTickets(mockedFile)).thenReturn(returnedTickets);

            when(mockedTicketRepository.findById("EA-10")).thenReturn(Optional.empty());
            when(mockedTicketRepository.findById("EA-9")).thenReturn(Optional.of(duplicateTicket));
        }

        @Test
        @DisplayName("stores issue types found")
        void importTicketsStoresIssuesTypes() {
            // Act
            sut.importTickets(mockedFile);

            // Assert
            verify(mockedIssueTypeRepository).saveAll(List.of(issueType));
        }

        @Test
        @DisplayName("stores resolutions found")
        void importTicketsStoresResolutions() {
            // Act
            sut.importTickets(mockedFile);

            // Assert
            verify(mockedResolutionRepository).saveAll(List.of(resolution));
        }

        @Test
        @DisplayName("stores statuses found")
        void importTicketsStoresStatuses() {
            // Act
            sut.importTickets(mockedFile);

            // Assert
            verify(mockedStatusRepository).saveAll(List.of(status));
        }

        @Test
        @DisplayName("stores labels found")
        void importTicketsStoresLabels() {
            // Act
            sut.importTickets(mockedFile);

            // Assert
            verify(mockedLabelRepository).saveAll(List.of(label1, label2));
        }

        @Test
        @DisplayName("stores users found")
        void importTicketsStoresUsers() {
            // Arrange
            ArgumentCaptor<List<UserReference>> argumentCaptor = ArgumentCaptor.forClass(List.class);
            List<UserReference> expectedUsers = List.of(user1, user2, user3, user4, user5);

            // Act
            sut.importTickets(mockedFile);

            // Assert
            verify(mockedUserReferenceRepository).saveAll(argumentCaptor.capture());
            List<UserReference> savedUsers = argumentCaptor.getValue();


            Assertions.assertEquals(expectedUsers.size(), savedUsers.size());
            Assertions.assertTrue(expectedUsers.containsAll(savedUsers));
            Assertions.assertTrue(savedUsers.containsAll(expectedUsers));
        }

        @Test
        @DisplayName("stores projects found")
        void importTicketsStoresProjects() {
            // Act
            sut.importTickets(mockedFile);

            // Assert
            verify(mockedProjectRepository).saveAll(List.of(project));
        }

        @Test
        @DisplayName("stores new tickets found")
        void importTicketsStoresTickets() {
            // Act
            sut.importTickets(mockedFile);

            // Assert
            verify(mockedTicketRepository).saveAll(List.of(ticket));
        }

        @Test
        @DisplayName("does not store duplicate tickets found")
        void importTicketsDoesNotStoreDuplicateTickets() {
            // Act
            sut.importTickets(mockedFile);

            // Assert
            verify(mockedArchiveService).deleteByTicket(duplicateTicket);
            verify(mockedTicketRepository).saveAll(List.of(ticket));
        }

        @Test
        @DisplayName("throws ImportFileNoNewTicketsException when all tickets are duplicates")
        void importTicketsThrowsExceptionWhenAllTicketsAreDuplicates() {
            // Arrange
            List<Ticket> returnedTickets = new ArrayList<>(List.of(duplicateTicket));
            when(mockedArchiveService.createTickets(mockedFile)).thenReturn(returnedTickets);

            // Act & Assert
            Assertions.assertThrows(ImportFileNoNewTicketsException.class, () -> sut.importTickets(mockedFile));
        }
    }
}