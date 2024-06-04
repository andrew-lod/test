package apple.backend.ticket;

import apple.backend.ticket.api.dto.PagedTicketsOverviewDto;
import apple.backend.ticket.api.dto.PinDto;
import apple.backend.ticket.api.dto.TicketDto;
import apple.backend.ticket.api.exceptions.TicketNotFoundException;
import apple.backend.ticket.domain.entity.Pin;
import apple.backend.ticket.domain.entity.Ticket;
import apple.backend.ticket.repository.*;
import apple.backend.ticket.domain.TicketService;
import apple.backend.ticket.domain.IArchiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class TicketServiceTest {
    @Mock
    private IArchiveService mockedArchiveService;
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
    @Mock
    private PinRepository mockedPinRepository;
    @Mock
    private Ticket mockedTicket;

    @InjectMocks
    private TicketService sut;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("getTicketsOverview()")
    class GetTicketsOverview {

        @Nested
        @DisplayName("Happy flow")
        class HappyFlow {

            @Test
            @DisplayName("calls ticketRepository.findAll(pageable) when keywordsString is empty")
            void getTicketsOverviewCallsTicketRepositoryWithoutKeywords() {
                // Arrange
                Pageable pageable = PageRequest.of(0, 15);
                Page<Ticket> pagedTickets = mock(Page.class);
                when(mockedTicketRepository.findAll(pageable)).thenReturn(pagedTickets);

                // Act
                sut.getTicketsOverview(0, "");

                // Assert
                verify(mockedTicketRepository).findAll(pageable);
                verify(mockedTicketRepository, never()).findAll(any(Specification.class), eq(pageable));
            }

//            @Test
//            @DisplayName("calls ticketRepository.findAll(specification, pageable) when keywordsString is not empty")
//            void getTicketsOverviewCallsTicketRepositoryWithKeywords() {
//                // Arrange
//                Pageable pageable = PageRequest.of(0, 15);
//                String keywordsString = "test1, test2";
//                List<String> keywords = List.of(keywordsString.split(",\\s*"));
//                Specification<Ticket> specifications = sut.specificationForKeywords(keywords);
//                Page<Ticket> pagedTickets = mock(Page.class);
//                when(mockedTicketRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pagedTickets);
//
//                // Act
//                sut.getTicketsOverview(0, keywordsString);
//
//                // Assert
//                verify(mockedTicketRepository).findAll(any(Specification.class), eq(pageable));
//                verify(mockedTicketRepository, never()).findAll(eq(pageable));
//            }

            @Test
            @DisplayName("correctly maps to PagedTicketsOverviewDto")
            void getTicketsOverviewMapsToPagedTicketsOverviewDto() {
                // Arrange
                Pageable pageable = PageRequest.of(0, 15);
                Page<Ticket> pagedTickets = mock(Page.class);
                Ticket ticket = new Ticket();
                ticket.setIssueKey("ISSUE-1");
                ticket.setSummary("Summary 1");
                when(pagedTickets.getContent()).thenReturn(List.of(ticket));
                when(pagedTickets.getNumber()).thenReturn(0);
                when(pagedTickets.getTotalPages()).thenReturn(1);
                when(pagedTickets.getTotalElements()).thenReturn(1L);
                when(mockedTicketRepository.findAll(pageable)).thenReturn(pagedTickets);

                // Act
                PagedTicketsOverviewDto result = sut.getTicketsOverview(0, "");

                // Assert
                assertEquals(1, result.getContent().size());
                assertEquals("ISSUE-1", result.getContent().get(0).getIssueKey());
                assertEquals("Summary 1", result.getContent().get(0).getSummary());
                assertEquals(0, result.getCurrentPage());
                assertEquals(1, result.getTotalPages());
                assertEquals(1L, result.getTotalElements());
            }
        }
    }

    @Nested
    @DisplayName("getTicket()")
    class GetTicket {
        @Nested
        @DisplayName("Happy flow")
        class HappyFlow {
            @Test
            @DisplayName("calls ticketRepository.findAll()")
            void getTicketCallsTicketRepository() {
                // Arrange
                when(mockedTicketRepository.findById(anyString())).thenReturn(Optional.of(new Ticket()));

                // Act
                sut.getTicket("EA-10000");

                // Assert
                verify(mockedTicketRepository).findById("EA-10000");
            }
        }
    }

    @Nested
    @DisplayName("importTickets()")
    class ImportTickets {
        @Nested
        @DisplayName("Happy flow")
        class HappyFlow {
            @Test
            @DisplayName("calls archiveService.importTickets()")
            void importTicketsCallsArchiveService() {
                // Arrange
                MultipartFile mockedFile = any(MultipartFile.class);

                // Act
                sut.importTickets(mockedFile);

                // Assert
                verify(mockedArchiveService).importTickets(mockedFile);
            }

            @Test
            @DisplayName("calls issueTypeRepository.saveAll()")
            void importTicketsCallsIssueTypeRepository() {
                // Arrange
                MultipartFile mockedFile = any(MultipartFile.class);

                // Act
                sut.importTickets(mockedFile);

                // Assert
                verify(mockedIssueTypeRepository).saveAll(any());
            }

            @Test
            @DisplayName("calls resolutionRepository.saveAll()")
            void importTicketsCallsResolutionRepository() {
                // Arrange
                MultipartFile mockedFile = any(MultipartFile.class);

                // Act
                sut.importTickets(mockedFile);

                // Assert
                verify(mockedResolutionRepository).saveAll(any());
            }

            @Test
            @DisplayName("calls statusRepository.saveAll()")
            void importTicketsCallsStatusRepository() {
                // Arrange
                MultipartFile mockedFile = any(MultipartFile.class);

                // Act
                sut.importTickets(mockedFile);

                // Assert
                verify(mockedStatusRepository).saveAll(any());
            }

            @Test
            @DisplayName("calls labelRepository.saveAll()")
            void importTicketsCallsLabelRepository() {
                // Arrange
                MultipartFile mockedFile = any(MultipartFile.class);

                // Act
                sut.importTickets(mockedFile);

                // Assert
                verify(mockedLabelRepository).saveAll(any());
            }

            @Test
            @DisplayName("calls userReferenceRepository.saveAll()")
            void importTicketsCallsUserReferenceRepository() {
                // Arrange
                MultipartFile mockedFile = any(MultipartFile.class);

                // Act
                sut.importTickets(mockedFile);

                // Assert
                verify(mockedUserReferenceRepository).saveAll(any());
            }

            @Test
            @DisplayName("calls projectRepository.saveAll()")
            void importTicketsCallsProjectRepository() {
                // Arrange
                MultipartFile mockedFile = any(MultipartFile.class);

                // Act
                sut.importTickets(mockedFile);

                // Assert
                verify(mockedProjectRepository).saveAll(any());
            }

            @Test
            @DisplayName("calls ticketRepository.saveAll()")
            void importTicketsCallsTicketRepository() {
                // Arrange
                MultipartFile mockedFile = any(MultipartFile.class);

                // Act
                sut.importTickets(mockedFile);

                // Assert
                verify(mockedTicketRepository).saveAll(any());
            }
        }
    }

    @Nested
    @DisplayName("getAllPins()")
    class GetAllPins {

        @Test
        @DisplayName("calls PinRepository.findAll() and maps to PinDto correctly")
        void getAllPinsCallsPinRepositoryAndMapsToPinDto() {
            // Arrange
            Pin pin1 = new Pin("ISSUE-1");
            Pin pin2 = new Pin("ISSUE-2");
            Ticket ticket1 = new Ticket();
            ticket1.setIssueKey("ISSUE-1");
            ticket1.setSummary("Summary 1");
            Ticket ticket2 = new Ticket();
            ticket2.setIssueKey("ISSUE-2");
            ticket2.setSummary("Summary 2");

            when(mockedPinRepository.findAll()).thenReturn(List.of(pin1, pin2));
            when(mockedTicketRepository.findById("ISSUE-1")).thenReturn(Optional.of(ticket1));
            when(mockedTicketRepository.findById("ISSUE-2")).thenReturn(Optional.of(ticket2));

            // Act
            List<PinDto> result = sut.getAllPins();

            // Assert
            verify(mockedPinRepository).findAll();
            verify(mockedTicketRepository).findById("ISSUE-1");
            verify(mockedTicketRepository).findById("ISSUE-2");
            assertEquals(2, result.size());
            assertEquals("ISSUE-1", result.get(0).getIssueKey());
            assertEquals("Summary 1", result.get(0).getSummary());
            assertEquals("ISSUE-2", result.get(1).getIssueKey());
            assertEquals("Summary 2", result.get(1).getSummary());
        }

        @Test
        @DisplayName("getAllPins() handles case where ticket is not found for a pin")
        void getAllPinsHandlesTicketNotFound() {
            // Arrange
            Pin pin1 = new Pin("ISSUE-1");
            Pin pin2 = new Pin("ISSUE-2");

            // Mocking pin repository
            when(mockedPinRepository.findAll()).thenReturn(List.of(pin1, pin2));

            // Mocking ticket repository
            Ticket ticket1 = new Ticket();
            ticket1.setIssueKey("ISSUE-1");
            ticket1.setSummary("Summary 1");

            when(mockedTicketRepository.findById("ISSUE-1")).thenReturn(Optional.of(ticket1));
            // Mocking the behavior for a ticket not found
            when(mockedTicketRepository.findById("ISSUE-2")).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TicketNotFoundException.class, () -> sut.getAllPins());
        }
    }


    @Nested
    @DisplayName("pinTicket()")
    class PinTicket {
        @Test
        @DisplayName("Pin a ticket by its issue key")
        void addTicketPin() {
            // Arrange
            String ticketIssueKey = "ISSUE-1";
            Ticket ticket = new Ticket();
            ticket.setIssueKey(ticketIssueKey);

            when(mockedTicketRepository.findById(ticketIssueKey)).thenReturn(Optional.of(ticket));

            // Act
            sut.addTicketPin(ticketIssueKey);

            // Assert
            verify(mockedTicketRepository).findById(ticketIssueKey);
            verify(mockedPinRepository).save(any(Pin.class));
        }

        @Test
        @DisplayName("throws TicketNotFoundException when ticket not found")
        void addTicketPinThrowsExceptionWhenTicketNotFound() {
            // Arrange
            String ticketIssueKey = "ISSUE-1";

            when(mockedTicketRepository.findById(ticketIssueKey)).thenReturn(Optional.empty());

            // Act & Assert
            TicketNotFoundException exception = assertThrows(TicketNotFoundException.class, () -> {
                sut.addTicketPin(ticketIssueKey);
            });

            assertEquals("Ticket with id: " + ticketIssueKey + " was not found", exception.getMessage());

            verify(mockedTicketRepository).findById(ticketIssueKey);
        }
    }

    @Nested
    @DisplayName("pinTicket()")
    class DeletePin {
        @Test
        @DisplayName("deletePin removes the pin if it exists")
        void deletePinRemovesExistingPin() {
            // Arrange
            String issueKey = "ISSUE-1";
            Pin pin = new Pin(issueKey);
            when(mockedPinRepository.findById(issueKey)).thenReturn(Optional.of(pin));

            // Act
            sut.deletePin(issueKey);

            // Assert
            verify(mockedPinRepository).delete(pin);
        }

        @Test
        @DisplayName("deletePin throws TicketNotFoundException if pin does not exist")
        void deletePinThrowsExceptionWhenPinNotFound() {
            // Arrange
            String issueKey = "ISSUE-1";
            when(mockedPinRepository.findById(issueKey)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TicketNotFoundException.class, () -> sut.deletePin(issueKey));
        }
    }
}

