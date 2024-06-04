package apple.backend.pinManagement.api;

import apple.backend.pinManagement.domain.PinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PinController {
    private PinService pinService;

    @Autowired
    public PinController(PinService pinService) {
        this.pinService = pinService;
    }

    @PostMapping("/pins")
    public void pinTicket(@RequestParam("issueKey") String ticketIssueKey){
        pinService.addTicketPin(ticketIssueKey);
    };

    @GetMapping("/pins")
    public List<PinDto> getAllPins() {
        return pinService.getAllPins();
    };

    @DeleteMapping("/pins")
    public void deletePin(@RequestParam("issueKey") String ticketIssueKey) {
        pinService.deletePin(ticketIssueKey);
    }
}
