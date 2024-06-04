package apple.backend.ticketMapping.api;

import apple.backend.ticketMapping.api.dto.MappingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class MappingController {
    private IMappingService mappingService;

    @Autowired
    public MappingController(IMappingService mappingService) {
        this.mappingService = mappingService;
    }

    @PostMapping("/mappings")
    public void setGlobalMapping(@RequestParam("mapping") MappingDto mapping) {
        mappingService.setGlobalMapping(mapping);
    }

    @PostMapping("/mappings/{id}")
    public void setTicketMapping(@RequestBody MappingDto mapping, @PathVariable("id") String ticketId) {
        mappingService.setTicketMapping(mapping, ticketId);
    }

    @PostMapping("/attributeMappings")
    public void setAttributeMapping(@RequestBody MappingDto mapping) {
        mappingService.setAttributeMapping(mapping);
    }
}
