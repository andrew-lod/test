package apple.backend.ticketManagement.api.dto;

import java.util.List;

public class PagedTicketsOverviewDto {
    private List<TicketOverviewDto> content;
    private long totalElements;
    private int currentPage;
    private int totalPages;

    public PagedTicketsOverviewDto(List<TicketOverviewDto> content, int currentPage, int totalPages, long totalElements) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public List<TicketOverviewDto> getContent() {
        return content;
    }

    public void setContent(List<TicketOverviewDto> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
