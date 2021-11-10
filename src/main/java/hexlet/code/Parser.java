package hexlet.code;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;


public class Parser {

    public static JsonNode getNodeDataFromFile(File file) throws IOException {
        String extension = FilenameUtils.getExtension(file.toString());
        JsonFactory factory = switch (extension) {
            case "yml" -> new YAMLFactory();
            default -> new JsonFactory();
        };

        ObjectMapper mapper = new ObjectMapper(factory);
        return mapper.readTree(file);
    }

    public static Set<String> getFields(JsonNode node) {
        Iterator<String> iterator = node.fieldNames();
        Set<String> fields = new HashSet<>();

        while (iterator.hasNext()) {
            String field = iterator.next();
            fields.add(field);
        }

        return fields;
    }
}
