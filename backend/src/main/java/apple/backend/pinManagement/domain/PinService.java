package apple.backend.pinManagement.domain;

import apple.backend.ticketManagement.api.exception.TicketNotFoundException;
import apple.backend.ticketManagement.domain.entity.Ticket;
import apple.backend.ticketManagement.repository.TicketRepository;
import apple.backend.pinManagement.repository.PinRepository;
import apple.backend.pinManagement.api.PinDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PinService {
    private PinRepository pinRepository;
    private TicketRepository ticketRepository;

    public PinService(PinRepository pinRepository, TicketRepository ticketRepository) {
        this.pinRepository = pinRepository;
        this.ticketRepository = ticketRepository;
    }

    public void addTicketPin(String ticketIssueKey) {
        ticketRepository.findById(ticketIssueKey).orElseThrow(new TicketNotFoundException(ticketIssueKey));

        Pin newPin = new Pin(ticketIssueKey);

        pinRepository.save(newPin);
    }

    public List<PinDto> getAllPins() {
        return pinRepository.findAll().stream()
                .map(pin -> {
                    Ticket ticket = ticketRepository.findById(pin.getTicketIssueKey())
                            .orElseThrow(() -> new TicketNotFoundException(pin.getTicketIssueKey()));

                    return new PinDto(pin.getTicketIssueKey());
                })
                .toList();
    }

    public void deletePin(String ticketIssueKey) {
        Pin pin = pinRepository.findById(ticketIssueKey)
                .orElseThrow(() -> new TicketNotFoundException(ticketIssueKey));
        pinRepository.delete(pin);
    }
}
