package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Differ {

    public static String generate(String filePath1, String filePath2) throws IOException {
        // Parse files to JsonNodes
        File file1 = getFileObj(filePath1);
        JsonNode node1 = getDataFromJsonFile(file1);

        File file2 = getFileObj(filePath2);
        JsonNode node2 = getDataFromJsonFile(file2);

        List<String> diffsList = getDiff(node1, node2);
        return diffsListToString(diffsList);
    }

    public static File getFileObj(String filePath) {
        Path path = Paths.get(filePath);
        return path.toAbsolutePath().toFile();
    }

    public static JsonNode getDataFromJsonFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        // Data data = objectMapper.readValue(file, Data.class);
        return mapper.readTree(file);
    }

    public static List<String> getDiff(JsonNode node1, JsonNode node2) {

        List<String> diffsList = new ArrayList<>();

        Set<String> fields = getFields(node1);
        fields.addAll(getFields(node2));
        List<String> sortedFields = new ArrayList<>(fields);
        Collections.sort(sortedFields);

        for (String field : sortedFields) {
            if (node1.has(field)) {
                JsonNode valueNode = node1.get(field);
                String fieldValueText = field + ": " + valueNode.asText();
                if (node2.has(field)) {
                    if (node2.get(field).equals(valueNode)) {
                        diffsList.add("    " + fieldValueText);
                    } else {
                        String fieldValueFromNode2 = field + ": " + node2.get(field).asText();
                        diffsList.add("  - " + fieldValueText);
                        diffsList.add("  + " + fieldValueFromNode2);
                    }
                } else {
                    diffsList.add("  - " + fieldValueText);
                }
            } else {
                String fieldValueFromNode2 = field + ": " + node2.get(field).asText();
                diffsList.add("  + " + fieldValueFromNode2);
            }
        }

        return diffsList;
    }

    private static Set<String> getFields(JsonNode node) {
        Iterator<String> iterator = node.fieldNames();
        Set<String> fields = new HashSet<>();

        while (iterator.hasNext()) {
            String field = iterator.next();
            fields.add(field);
        }

        return fields;
    }

    private static String diffsListToString(List<String> diffsList) {
        StringBuilder sb = new StringBuilder("{\n");
        for (String diff : diffsList) {
            sb.append(diff + "\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
