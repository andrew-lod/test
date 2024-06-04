package apple.backend.ticketMapping.parser;

import apple.backend.ticketManagement.api.exception.ImportFileUnsupportedFormatException;
import apple.backend.ticketMapping.domain.entity.Route;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("archiveParser")
public class JsonObjectParser extends ObjectParser {
    private ObjectMapper mapper;

    @Autowired
    public JsonObjectParser(ObjectMapper mapper) {
        super(MediaType.APPLICATION_JSON.toString());
        this.mapper = mapper;
    }

    @Override
    public boolean supports(String contentType) {
        return MediaType.APPLICATION_JSON.toString().equals(contentType);
    }

    @Override
    public List<String> objectsAsString(MultipartFile file) {
        try {
            JsonNode rootNode = mapper.readTree(file.getInputStream());
            List<String> objects = new ArrayList<>();
            rootNode.forEach(node -> objects.add(node.toString()));
            return objects;
        } catch (IOException e) {
            throw new ImportFileUnsupportedFormatException("Failed to read JSON file");
        }
    }

    @Override
    public List<String> getArray(String node, Route route) {
        if(route == null || node == null || node.isEmpty()) return null;

        try {
            JsonNode jsonNode = mapper.readTree(node);
            return getArrayRecursive(jsonNode, route);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getArrayRecursive(JsonNode currentNode, Route route) {
        if (route.getChildRoute() != null) {
            currentNode = currentNode.get(route.getPath());
            return getArrayRecursive(currentNode, route.getChildRoute());
        }

        List<JsonNode> results = new ArrayList<>();

        if(route.getPathPattern() != null) {
            String baseKey = route.getPath();
            int index = Integer.parseInt(route.getPathPattern());
            while(currentNode.has(baseKey + index)) {
                results.add(currentNode.get(baseKey + index));
                index++;
            }
        } else {
            JsonNode targetNode = currentNode.get(route.getPath());
            targetNode.forEach(node -> results.add(node));
        }
        return results.stream()
                .filter(Objects::nonNull)
                .map(jNode -> {
                    if (jNode.toString().startsWith("0")) {
                        return "\"" + jNode + "\"";
                    }
                    return jNode.toString();
                })
                .toList();
    }

    @Override
    public String getValue(String node,  Route route) {
        if(route == null || node == null || node.isEmpty()) return null;

        try {
            JsonNode jsonNode = mapper.readTree(node);
            return getValueRecursive(jsonNode, route);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getValueRecursive(JsonNode currentNode, Route route) {
        if(currentNode == null) return null;
        if (route.getPath() != null) {
            currentNode = currentNode.get(route.getPath());
        }
        if (route.getChildRoute() != null) {
            return getValueRecursive(currentNode, route.getChildRoute());
        }

        if(currentNode == null) return null;
        return applyProcessingRule(currentNode.asText(), route.getProcessingRule());

    }

    @Override
    public Date getDate(String node, Route route) {
        if(route == null || node == null || node.isEmpty()) return null;

        try {
            String dateString = getValue(node, route);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH);//(route.getProcessingRule());
            if(dateString == null || dateString.isEmpty()) return null;
            else return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String applyProcessingRule(String value, String rule) {
        if (rule != null && !rule.isEmpty()) {
            Pattern pattern = Pattern.compile(rule, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return value;
    }
}

