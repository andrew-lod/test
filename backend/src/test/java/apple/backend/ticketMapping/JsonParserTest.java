//package apple.backend.ticketArchiveMapping;
//
//import apple.backend.ticketManagement.domain.entity.ArchivedTicket;
//import apple.backend.ticketArchiveMapping.parser.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class JsonParserTest {
//    @Mock
//    private ObjectMapper mapper;
//
//    @InjectMocks
//    private JsonParser sut;
//
//    @BeforeEach
//    public void setup () {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Nested
//    @DisplayName("SplitIntoObjects()")
//    class SplitIntoObjects {
//            @Test
//            @DisplayName("calls getFileTree() and creates a list of ArchivedTickets")
//            void SplitIntoObjects() throws IOException {
//                // Arrange
//                MultipartFile file = mock(MultipartFile.class);
//                JsonNode node = mock(JsonNode.class);
//                Iterator<JsonNode> iterator = mock(Iterator.class);
//                JsonNode childNode = mock(JsonNode.class);
//                List<ArchivedTicket> tickets = new ArrayList<>();
//
//                // Act
//                when(sut.getFileTree(file)).thenReturn(node);
//                when(node.iterator()).thenReturn(iterator);
//                when(iterator.hasNext()).thenReturn(true, false);
//                when(iterator.next()).thenReturn(childNode);
//                when(childNode.toString()).thenReturn("{}");
//
//                // Assert
//                List<ArchivedTicket> result = sut.splitIntoObjects(file);
//                assertNotNull(result);
//            }
//    }
//
//    @Nested
//    @DisplayName("GetFileTree()")
//    class GetFileTree {
//            @Test
//            @DisplayName("returns a JsonNode")
//            void GetFileTree() throws IOException {
//                // Arrange
//                MultipartFile file = mock(MultipartFile.class);
//                JsonNode node = mock(JsonNode.class);
//                InputStream inputStream = new ByteArrayInputStream("{}".getBytes());
//
//                when(file.getInputStream()).thenReturn(inputStream);
//                when(mapper.readTree(inputStream)).thenReturn(node);
//
//                // Act
//                JsonNode result = sut.getFileTree(file);
//
//                // Assert
//                assertEquals(node, result);
//            }
//
//            @Test
//            @DisplayName("returns null when an exception is thrown")
//            void GetFileTree() throws IOException {
//                // Arrange
//                MultipartFile file = mock(MultipartFile.class);
//                InputStream inputStream = new ByteArrayInputStream("{}".getBytes());
//
//                when(file.getInputStream()).thenReturn(inputStream);
//                when(mapper.readTree(inputStream)).thenThrow(JsonProcessingException.class);
//
//                // Act
//                JsonNode result = sut.getFileTree(file);
//
//                // Assert
//                assertNull(result);
//            }
//    }
//
//    @Nested
//    @DisplayName("ConvertToTickets()")
//    class ConvertToTickets {
//
//            @Test
//            @DisplayName("creates a list of Tickets")
//            void ConvertToTickets() throws JsonProcessingException {
//
//                // Arrange
//                ArchivedTicket archivedTicket = new ArchivedTicket("{}");
//                List<ArchivedTicket> archivedTickets = List.of(archivedTicket);
//                JsonNode node = mock(JsonNode.class);
//                when(mapper.readTree(anyString())).thenReturn(node);
//
//                // Set up the mock for all the necessary fields
//                when(node.get("Issue key")).thenReturn(mock(JsonNode.class));
//                when(node.get("Summary")).thenReturn(mock(JsonNode.class));
//                when(node.get("Description")).thenReturn(mock(JsonNode.class));
//                when(node.get("Created")).thenReturn(mock(JsonNode.class));
//                when(node.get("Updated")).thenReturn(mock(JsonNode.class));
//                when(node.get("Issue Type")).thenReturn(mock(JsonNode.class));
//                when(node.get("Resolution")).thenReturn(mock(JsonNode.class));
//                when(node.get("Status")).thenReturn(mock(JsonNode.class));
//                when(node.get("Creator")).thenReturn(mock(JsonNode.class));
//                when(node.get("Reporter")).thenReturn(mock(JsonNode.class));
//                when(node.get("Assignee")).thenReturn(mock(JsonNode.class));
//                when(node.get("Labels")).thenReturn(mock(JsonNode.class));
//                when(node.get("Project lead")).thenReturn(mock(JsonNode.class));
//                when(node.get("Project name")).thenReturn(mock(JsonNode.class));
//                when(node.get("Project type")).thenReturn(mock(JsonNode.class));
//
//                // Mock the return values for the text fields
//                when(node.get(anyString())).thenReturn(mock(JsonNode.class));
//                when(node.get("Issue key").asText()).thenReturn("issueKey");
//                when(node.get("Summary").asText()).thenReturn("summary");
//                when(node.get("Description").asText()).thenReturn("description");
//                when(node.get("Created").asText()).thenReturn("2011-12-09 13:59");
//                when(node.get("Updated").asText()).thenReturn("2011-12-10 14:00");
//                when(node.get("Issue Type").asText()).thenReturn("issueType");
//                when(node.get("Resolution").asText()).thenReturn("resolution");
//                when(node.get("Status").asText()).thenReturn("status");
//                when(node.get("Creator").asText()).thenReturn("creator");
//                when(node.get("Reporter").asText()).thenReturn("reporter");
//                when(node.get("Assignee").asText()).thenReturn("assignee");
//                when(node.get("Labels").asText()).thenReturn("labels");
//                when(node.get("Project lead").asText()).thenReturn("projectLead");
//                when(node.get("Project name").asText()).thenReturn("projectName");
//                when(node.get("Project type").asText()).thenReturn("projectType");
//
//                // Act
//                List<Ticket> resultSet = sut.convertToTickets(archivedTickets);
//
//                // Assert
//                assertNotNull(resultSet);
//                assertFalse(resultSet.isEmpty());
//                assertEquals(1, resultSet.size());
//                assertEquals("issueKey", resultSet.get(0).getIssueKey());
//                assertEquals("summary", resultSet.get(0).getSummary());
//                assertEquals("2011-12-09 13:59", resultSet.get(0).getCreatedAt().toString());
//                assertEquals("2011-12-10 14:00", resultSet.get(0).getResolvedAt().toString());
//            }
//
//
//            @Test
//            @DisplayName("returns an empty list when JsonProcessingException is thrown")
//            void ConvertToTickets_JsonProcessingException() throws JsonProcessingException {
//                // Arrange
//                ArchivedTicket archivedTicket = new ArchivedTicket("{}");
//                List<ArchivedTicket> archivedTickets = List.of(archivedTicket);
//
//                when(mapper.readTree(anyString())).thenThrow(JsonProcessingException.class);
//
//                // Act
//                List<Ticket> result = sut.convertToTickets(archivedTickets);
//
//                // Assert
//                assertNotNull(result);
//                assertTrue(result.isEmpty());
//            }
//    }
//}
//
