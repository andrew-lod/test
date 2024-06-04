package apple.backend.ticketManagement.api;

import apple.backend.filter.dto.FilterDto;
import apple.backend.ticketManagement.api.dto.FiltersDto;
import apple.backend.ticketManagement.api.dto.PagedTicketsOverviewDto;

import apple.backend.ticketManagement.api.dto.TicketDto;
import apple.backend.ticketManagement.domain.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class TicketController {
    private ITicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/tickets")
    public void importTickets(@RequestParam("file") MultipartFile file) {
        ticketService.importTickets(file);
    }

    /**
     * Return a {@link List} of {@link TicketDto} instances.
     *
     * @param page     The current page
     * @param keywords The filter is a string that is used to filter the {@link List} of {@link TicketDto} instances.
     * @return A {@link List} of {@link TicketDto} instances.
     */
    @GetMapping("/tickets")
    public PagedTicketsOverviewDto getTicketsOverview(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keywords", defaultValue = "") String keywords,
            @RequestParam(name = "issueType", defaultValue = "") String issueType,
            @RequestParam(name = "resolution", defaultValue = "") String resolution,
            @RequestParam(name = "status", defaultValue = "") String status,
            @RequestParam(name = "label", defaultValue = "") String label
    ) {

        return ticketService.getTicketsOverview(page, keywords, issueType, resolution, status, label);
    }

    /**
     * Return a complete {@link TicketDto} with the given id.
     *
     * @return A complete {@link TicketDto} instance.
     * @PathVariable id The id of the {@link TicketDto} to be returned.
     */
    @GetMapping("/tickets/{id}")
    public TicketDto getTicket(@PathVariable String id) {
        return ticketService.getTicket(id);
    }

    //  query = "keywords=EA, hoi, test&label=hoi"

    /**
     * Save a filter with a given name and query.
     *
     * @param name   The name of the filter.
     * @param filter The filter query.
     */
    @PostMapping("/tickets/saveFilter")
    public void saveFilter(@RequestParam String name, @RequestParam String filter) {
        if (name == null || name.isEmpty()) {
            System.out.println("Filter name is null or empty");
        } else if (filter == null || filter.isEmpty()) {
            System.out.println("Filter query is null or empty");
        } else {
            ticketService.saveFilter(name, filter);
            System.out.println("Filter saved successfully");
        }
    }

    @DeleteMapping("/filters")
    public void deleteFilter (@RequestParam Integer id) {
        ticketService.deleteFilter(id);
    }

    @GetMapping("/filters")
    public List<FilterDto> getAllFilters() {
        return ticketService.getAllFilters();
    }

    @PostMapping("/tickets/refresh")
    public void refreshTickets() {
        ticketService.refreshTickets();
    }

    @GetMapping("/tickets/filters")
    public FiltersDto getTicketFilters() {
        return ticketService.getTicketFilters();
    }
}
