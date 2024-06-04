package apple.backend.ticketManagement.api;

import apple.backend.filter.dto.FilterDto;
import apple.backend.ticketManagement.api.dto.FiltersDto;
import apple.backend.ticketManagement.api.dto.PagedTicketsOverviewDto;
import apple.backend.ticketManagement.api.dto.TicketDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITicketService {
    void importTickets(MultipartFile file);

    void refreshTickets();

    TicketDto getTicket(String id);

    FiltersDto getTicketFilters();

    void deleteFilter(Integer id);

    void saveFilter(String name, String filter);

    List<FilterDto> getAllFilters();

    PagedTicketsOverviewDto getTicketsOverview(int page, String keywords, String issueType, String resolution, String status, String label);
}
