package apple.backend.ticketMapping.domain;

import apple.backend.ticketManagement.domain.entity.CustomAttribute;
import apple.backend.ticketManagement.repository.CustomAttributeRepository;
import apple.backend.ticketMapping.api.IMappingService;
import apple.backend.ticketMapping.api.dto.MappingDto;
import apple.backend.ticketMapping.api.dto.RouteDto;
import apple.backend.ticketMapping.domain.entity.CustomAttributeMapping;
import apple.backend.ticketMapping.domain.entity.Route;
import apple.backend.ticketMapping.repository.CustomAttributeMappingRepository;
import apple.backend.ticketMapping.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MappingService implements IMappingService {
    CustomAttributeRepository customAttributeRepository;
    CustomAttributeMappingRepository customAttributeMappingRepository;
    RouteRepository routeRepository;

    @Autowired
    public MappingService(CustomAttributeRepository customAttributeRepository ,CustomAttributeMappingRepository customAttributeMappingRepository, RouteRepository routeRepository) {
        this.customAttributeRepository = customAttributeRepository;
        this.customAttributeMappingRepository = customAttributeMappingRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    public void setGlobalMapping(MappingDto mapping) {

    }

    @Override
    public void setTicketMapping(MappingDto mapping, String ticketId) {

    }

    @Override
    public void setAttributeMapping(MappingDto mapping) {
        CustomAttributeMapping customAttributeMapping = mapDtoToEntity(mapping);
        saveRoutes(customAttributeMapping.getRoute());
        customAttributeMappingRepository.save(customAttributeMapping);
    }

    private Route saveRoutes(Route route) {
        if (route.getChildRoute() != null)
            saveRoutes(route.getChildRoute());
        return routeRepository.save(route);
    }

    private CustomAttributeMapping mapDtoToEntity(MappingDto mappingDto) {
        CustomAttribute customAttribute = customAttributeRepository.findById(mappingDto.getKeyName())
                .orElseThrow(() -> new IllegalArgumentException("Custom attribute not found"));

        CustomAttributeMapping customAttributeMapping = new CustomAttributeMapping();
        customAttributeMapping.setCustomAttribute(customAttribute);
        customAttributeMapping.setRoute(mapDtoToEntity(mappingDto.getRoute()));

        return customAttributeMapping;
    }

    private Route mapDtoToEntity(RouteDto routeDto) {
        Route route = new Route();
        route.setPath(routeDto.getPath());
        route.setPathPattern(routeDto.getKeyProcessing());
        route.setProcessingRule(routeDto.getValueProcessing());
        if (routeDto.getNext() != null)
            route.setChildRoute(mapDtoToEntity(routeDto.getNext()));
        return route;
    }

}
