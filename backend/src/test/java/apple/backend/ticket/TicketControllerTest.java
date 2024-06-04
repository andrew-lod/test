package apple.backend.ticket;

import apple.backend.ticket.api.TicketControllerAdvice;
import apple.backend.ticket.api.dto.PinDto;
import apple.backend.ticket.api.exceptions.TicketNotFoundException;
import apple.backend.ticket.domain.TicketService;
import apple.backend.ticket.api.TicketController;
import apple.backend.ticketManagement.api.exception.TicketNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketControllerTest {

    @Mock
    private TicketService fixture;
    private MockMvc mockMvc;
    @InjectMocks
    private TicketController sut;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sut)
                .setControllerAdvice(new TicketControllerAdvice())
                .build();
    }

    @Nested
    @DisplayName("getTicketsOverview(amount, offset, filter)")
    class getTicketsOverview {

        @Test
        @DisplayName("calls TicketService.getTicketsOverview(amount, offset, filter)")
        void multipleTicketsCallsGetTickets() {
            // Act
            sut.getTicketsOverview(0, "");

            // Assert
            verify(fixture).getTicketsOverview(0, "");
        }

    }

    @Nested
    @DisplayName("getTicket(id)")
    class getTicket {

        @Test
        @DisplayName("calls TicketService.getTicket(id)")
        void singleTicketCallsGetTicket() {
            // Arrange
            String id = "1";
            // Act
            sut.getTicket(id);

            // Assert
            verify(fixture).getTicket(id);
        }
    }

    @Nested
    @DisplayName("Exception Handling")
    class ExceptionHandling {

        @Test
        @DisplayName("TicketNotFoundException returns 404 HTTP Response code")
        void handleTicketsNotFoundException() throws Exception {
            // Arrange
            MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "content".getBytes());
            String id = "EA-10";
            doThrow(new TicketNotFoundException(id)).when(fixture).importTickets(Mockito.any(MultipartFile.class));

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.multipart("/tickets").file(file))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().string("Ticket with id: EA-10 was not found"));
        }
    }

    @Nested
    @DisplayName("pinTicket(issueKey)")
    class PinTicket {

        @Test
        @DisplayName("calls TicketService.addTicketPin(issueKey)")
        void callsAddTicketPin() throws Exception {
            // Arrange
            String issueKey = "ISSUE-1";

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.post("/pins")
                            .param("issueKey", issueKey)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(fixture).addTicketPin(issueKey);
        }

        @Test
        @DisplayName("returns 404 when TicketNotFoundException is thrown")
        void returns404WhenTicketNotFoundExceptionThrown() throws Exception {
            // Arrange
            String issueKey = "ISSUE-1";
            doThrow(new TicketNotFoundException(issueKey)).when(fixture).addTicketPin(issueKey);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.post("/pins")
                            .param("issueKey", issueKey)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Ticket with id: ISSUE-1 was not found"));
    @DisplayName("createCustomAttribute()")
    class CreateCustomAttribute {
        @Test
        @DisplayName("valid key returns 200 OK")
        void createCustomAttributeWithValidKey() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/tickets/customValue")
                            .param("key", "testKey"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("no key provided returns 400 Bad Request")
        void createCustomAttributeWithoutKey() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/tickets/customValue"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("getAllPins()")
    class GetAllPins {

        @Test
        @DisplayName("calls TicketService.getAllPins() and returns multiple pins")
        void callsGetAllPinsWithMultiplePins() throws Exception {
            // Arrange
            var pinDto1 = new PinDto("ISSUE-1");
            var pinDto2 = new PinDto("ISSUE-2");
            var pinDto3 = new PinDto("ISSUE-3");
            List<PinDto> pinDtos = List.of(pinDto1, pinDto2, pinDto3);

            when(fixture.getAllPins()).thenReturn(pinDtos);

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.get("/pins")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].issueKey").value("ISSUE-1"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].issueKey").value("ISSUE-2"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].issueKey").value("ISSUE-3"));

            verify(fixture).getAllPins();
        }

        @Test
        @DisplayName("returns empty list when no pins are found")
        void returnsEmptyListWhenNoPinsFound() throws Exception {
            // Arrange
            when(fixture.getAllPins()).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(MockMvcRequestBuilders.get("/pins")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

            verify(fixture).getAllPins();
        }
    }

    @Nested
    @DisplayName("deletePin()")
    class DeletePin {

        @Test
        @DisplayName("calls TicketService.deletePin() and returns no content")
        void callsDeletePin() throws Exception {
            // Arrange
            String issueKey = "ISSUE-1";

            // Act & Assert
            mockMvc.perform(delete("/pins")
                            .param("issueKey", issueKey)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            verify(fixture).deletePin(issueKey);
        }

        @Test
        @DisplayName("returns 404 when pin is not found")
        void returns404WhenPinNotFound() throws Exception {
            // Arrange
            String issueKey = "ISSUE-1";
            doThrow(new TicketNotFoundException(issueKey)).when(fixture).deletePin(issueKey);

            // Act & Assert
            mockMvc.perform(delete("/pins")
                            .param("issueKey", issueKey)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Ticket with id: ISSUE-1 was not found"));

            verify(fixture).deletePin(issueKey);
    @DisplayName("addAttributeMapping()")
    class AddAttributeMapping {
        @Test
        @DisplayName("valid mapping returns 200 OK")
        void addAttributeMappingWithValidMapping() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/tickets/customValue/Addmapping")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"key\":\"testKey\", \"route\":\"testRoute\"}"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("no mapping provided returns 400 Bad Request")
        void addAttributeMappingWithoutMapping() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/tickets/customValue/Addmapping")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
