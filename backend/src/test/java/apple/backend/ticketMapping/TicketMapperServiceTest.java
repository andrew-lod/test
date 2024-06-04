package apple.backend.ticketMapping;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import apple.backend.ticketManagement.api.exception.ImportFileUnsupportedFormatException;
import apple.backend.ticketManagement.domain.entity.Ticket;
import apple.backend.ticketManagement.domain.entity.ArchivedTicket;
import apple.backend.ticketMapping.domain.IObjectParser;
import apple.backend.ticketMapping.domain.TicketMapperService;
import apple.backend.ticketMapping.domain.entity.ArchivedTicketMapping;
import apple.backend.ticketMapping.domain.entity.GlobalMapping;
import apple.backend.ticketMapping.domain.entity.Route;
import apple.backend.ticketMapping.repository.ArchivedTicketMappingRepository;
import apple.backend.ticketMapping.repository.GlobalMappingRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.Example;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public class TicketMapperServiceTest {

    @Mock
    private GlobalMappingRepository mockedGlobalMappingRepository;
    @Mock
    private ArchivedTicketMappingRepository mockedArchivedTicketMappingRepository;
    @Mock
    private IObjectParser mockedArchiveParser;
    @InjectMocks
    private TicketMapperService sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("getArchivedTickets(file)")
    class GetArchivedTicketsTests {
        private MultipartFile file;
        private String archivedTicketObject1, archivedTicketObject2;

        @BeforeEach
        void setUp() {
            file = mock(MultipartFile.class);
            archivedTicketObject1 = "{\"issueKey\":\"TV-10\"}";
            archivedTicketObject2 = "{\"issueKey\":\"TV-12\"}";
        }

        @Test
        @DisplayName("file with single ticket returns list with the ArchivedTicket")
        void shouldParseFileAndReturnArchivedTickets() {
            // Arrange
            when(mockedArchiveParser.objectsAsString(file)).thenReturn(List.of(archivedTicketObject1));

            // Act
            List<Ticket> result = sut.createTickets(file);

            // Assert
            verify(mockedArchiveParser).objectsAsString(file);
            assertEquals(1, result.size());
            assertEquals(archivedTicketObject1, result.get(0).getArchivedTicket().getTicketObject());
        }

        @Test
        @DisplayName("file with multiple tickets returns list of ArchivedTickets")
        void shouldParseFileWithMultipleTicketsAndReturnArchivedTickets() {
            // Arrange
            when(mockedArchiveParser.objectsAsString(file)).thenReturn(List.of(archivedTicketObject1, archivedTicketObject2));

            // Act
            List<Ticket> result = sut.createTickets(file);

            // Assert
            assertEquals(2, result.size());
            assertEquals(archivedTicketObject1, result.get(0).getArchivedTicket().getTicketObject());
            assertEquals(archivedTicketObject2, result.get(1).getArchivedTicket().getTicketObject());
            verify(mockedArchiveParser).objectsAsString(file);
        }
    }

    @Nested
    @DisplayName("convertToTickets(archivedTickets)")
    class ConvertToTicketsTests {
        private ArchivedTicket ticket;
        private GlobalMapping issueKeyMapping;
        private ArchivedTicketMapping archivedTicketMapping;

        @BeforeEach
        void setUp() {
            ticket = new ArchivedTicket("{ticketObject}");

            issueKeyMapping = new GlobalMapping(TicketMapperService.TICKET_ISSUE_KEY, new Route());

            archivedTicketMapping = new ArchivedTicketMapping();
            archivedTicketMapping.setKey(TicketMapperService.PROJECT_NAME);
            archivedTicketMapping.setRoute(new Route());
        }

        @Test
        @DisplayName("archived tickets get mapped to tickets")
        void archivedTicketsToTickets() {
            // Arrange
            when(mockedGlobalMappingRepository.findAll()).thenReturn(List.of(issueKeyMapping));
            when(mockedArchivedTicketMappingRepository.findAll(any(Example.class))).thenReturn(List.of(archivedTicketMapping));
            when(mockedArchiveParser.getValue(anyString(), nullable(Route.class))).thenReturn("value");
            when(mockedArchiveParser.getDate(anyString(), nullable(Route.class))).thenReturn(new Date());
            when(mockedArchiveParser.getArray(anyString(), nullable(Route.class))).thenReturn(List.of("value"));

            Ticket ticket1 = new Ticket();
            ticket1.setArchivedTicket(ticket);
            // Act
            sut.populateTickets(List.of(ticket1));

            // Assert
            verify(mockedGlobalMappingRepository).findAll();
            verify(mockedArchivedTicketMappingRepository).findAll(any(Example.class));

        }

        @Test
        @DisplayName("no global mapping set for issue key")
        void noGlobalMappingForIssueKey() {
            // Arrange
            when(mockedGlobalMappingRepository.findAll()).thenReturn(List.of());

            // Act & Assert
            assertThrows(ImportFileUnsupportedFormatException.class, () -> sut.convertToTickets(List.of(ticket)));
        }

        @Test
        @DisplayName("ticket skipped when required field missing")
        void skippedTicketMissingRequired() {
            // Arrange
            when(mockedGlobalMappingRepository.findAll()).thenReturn(List.of(issueKeyMapping));
            when(mockedArchivedTicketMappingRepository.findAll(any(Example.class))).thenReturn(List.of(archivedTicketMapping));
            when(mockedArchiveParser.getValue(anyString(), nullable(Route.class))).thenReturn(null);

            Ticket ticket1 = new Ticket();
            ticket1.setArchivedTicket(ticket);

            // Act
            List<Ticket> result = sut.populateTickets(List.of(ticket1));

            // Assert
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("comment skipped when required field missing")
        void skippedCommentMissingRequired() {
            // Arrange
            when(mockedGlobalMappingRepository.findAll()).thenReturn(List.of(issueKeyMapping));
            when(mockedArchivedTicketMappingRepository.findAll(any(Example.class))).thenReturn(List.of(archivedTicketMapping));

            when(mockedArchiveParser.getValue(anyString(), nullable(Route.class))).thenReturn("value");

            when(mockedArchiveParser.getDate(anyString(), nullable(Route.class))).thenReturn(null);
            when(mockedArchiveParser.getArray(anyString(), nullable(Route.class))).thenReturn(List.of("value"));

            // Act
            List<Ticket> result = sut.convertToTickets(List.of(ticket));

            // Assert
            assertNull(result.get(0).getComments());
        }

        @Test
        @DisplayName("attachment skipped when required fields missing")
        void skippedAttachmentMissingRequired() {

        }
    }
}