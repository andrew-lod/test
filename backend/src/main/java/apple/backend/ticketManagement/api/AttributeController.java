package apple.backend.ticketManagement.api;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class AttributeController {
    private IAttributeService attributeService;

    @Autowired
    public AttributeController(IAttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @PostMapping("/tickets/attributes")
    public void createTicketAttribute(@RequestParam("key") String key){
        attributeService.createCustomAttribute(key);
    }

    @DeleteMapping("/tickets/attributes/{key}")
    public void deleteTicketAttribute(@PathParam("key") String key){
        attributeService.deleteCustomAttribute(key);
    }
}
