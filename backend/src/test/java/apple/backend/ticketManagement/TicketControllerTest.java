package apple.backend.ticketManagement;

import apple.backend.ticketManagement.api.exception.ImportFileNoNewTicketsException;
import apple.backend.ticketManagement.api.exception.ImportFileUnsupportedFormatException;
import apple.backend.ticketManagement.domain.TicketService;
import apple.backend.ticketManagement.api.TicketController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService mockedTicketService;

    @BeforeEach
    public void setup() {
        openMocks(this);
    }

    @Nested
    @DisplayName("importTickets()")
    class ImportTickets {
        @Test
        @DisplayName("valid file returns 200 OK")
        void importTicketsWithValidFile() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "Some content".getBytes());
            doNothing().when(mockedTicketService).importTickets(any(MultipartFile.class));

            mockMvc.perform(MockMvcRequestBuilders.multipart("/tickets")
                            .file(file)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("no new tickets in file returns 400 Bad Request")
        void importTicketsWithNoNewTickets() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "Some content".getBytes());
            doThrow(new ImportFileNoNewTicketsException()).when(mockedTicketService).importTickets(any(MultipartFile.class));

            mockMvc.perform(MockMvcRequestBuilders.multipart("/tickets")
                            .file(file)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(ImportFileNoNewTicketsException.MESSAGE));
        }

        @Test
        @DisplayName("unsupported file format returns 400 Bad Request")
        void importTicketsWithUnsupportedFormat() throws Exception {
            MockMultipartFile file = new MockMultipartFile("file", "filename.unknown", "text/plain", "Some strange content".getBytes());
            doThrow(new ImportFileUnsupportedFormatException("Unsupported file format.")).when(mockedTicketService).importTickets(any(MultipartFile.class));

            mockMvc.perform(MockMvcRequestBuilders.multipart("/tickets")
                            .file(file)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string("Unsupported file format."));
        }

        @Test
        @DisplayName("no file provided returns 400 Bad Request")
        void importTicketsWithoutFile() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post("/tickets")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isBadRequest());
        }
    }
}
