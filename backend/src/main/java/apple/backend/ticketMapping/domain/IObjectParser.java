package apple.backend.ticketMapping.domain;

import apple.backend.ticketMapping.domain.entity.Route;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface IObjectParser {
    boolean supports(String file);

    List<String> objectsAsString(MultipartFile file);

    String getValue(String node, Route route);

    List<String> getArray(String node, Route route);

    Date getDate(String node, Route route);
}
