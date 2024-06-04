package apple.backend.ticketManagement.domain;

import apple.backend.ticketManagement.api.IAttributeService;
import apple.backend.ticketManagement.domain.entity.CustomAttribute;
import apple.backend.ticketManagement.repository.CustomAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeService implements IAttributeService {
    CustomAttributeRepository customAttributeRepository;

    @Autowired
    public AttributeService(CustomAttributeRepository customAttributeRepository) {
        this.customAttributeRepository = customAttributeRepository;
    }


    @Override
    public void createCustomAttribute(String key) {
        customAttributeRepository.save(new CustomAttribute(key));
    }

    @Override
    public void deleteCustomAttribute(String key) {

    }
}
