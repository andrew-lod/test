package apple.backend.ticketMapping.api;

import apple.backend.ticketMapping.api.dto.MappingDto;

public interface IMappingService {

    void setGlobalMapping(MappingDto mapping);

    void setTicketMapping(MappingDto mapping, String ticketId);

    void setAttributeMapping(MappingDto mapping);
}
