package hexlet.code;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.Map;


public class Parser {

    public static Map<String, Object> parse(String data, String fileType) throws JsonProcessingException {
        JsonFactory factory = switch (fileType) {
            case "yml" -> new YAMLFactory();
            case "json" -> new JsonFactory();
            default -> throw new RuntimeException("given file extension is not supported");
        };

        ObjectMapper mapper = new ObjectMapper(factory);
        return mapper.readValue(data, new TypeReference<Map<String, Object>>() { });
    }
}
