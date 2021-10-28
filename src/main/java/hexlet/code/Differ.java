package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Differ {

    public enum DiffStatus {
        EQUAL,
        ADDED,
        REMOVED
    }

    public static String generate(String filePath1, String filePath2, String format) throws IOException {
        // Parse files to JsonNodes
        File file1 = getFileObj(filePath1);
        // Map<String, String> dataFile1 = Parser.getDataFromFile(file1);
        JsonNode dataFile1 = Parser.getNodeDataFromFile(file1);


        File file2 = getFileObj(filePath2);
        // Map<String, String> dataFile2 = Parser.getDataFromFile(file2);
        JsonNode dataFile2 = Parser.getNodeDataFromFile(file2);

        List<String> diffsList = getDiff(dataFile1, dataFile2);
        return diffsListToString(diffsList);
    }

    public static File getFileObj(String filePath) {
        Path path = Paths.get(filePath);
        return path.toAbsolutePath().toFile();
    }

    public static List<String> getDiff(Map<String, String> data1, Map<String, String> data2) {

        List<String> diffsList = new ArrayList<>();

        Set<String> fields = Parser.getFields(data1);
        fields.addAll(Parser.getFields(data2));
        List<String> sortedFields = new ArrayList<>(fields);
        Collections.sort(sortedFields);

        for (String field : sortedFields) {
            if (data1.containsKey(field)) {
                String value = data1.get(field);
                String fieldValueText = field + ": " + value;
                if (data2.containsKey(field)) {
                    if (data2.get(field).equals(value)) {
                        diffsList.add("    " + fieldValueText);
                    } else {
                        String fieldValueFromData2 = field + ": " + data2.get(field);
                        diffsList.add("  - " + fieldValueText);
                        diffsList.add("  + " + fieldValueFromData2);
                    }
                } else {
                    diffsList.add("  - " + fieldValueText);
                }
            } else {
                String fieldValueFromNode2 = field + ": " + data2.get(field);
                diffsList.add("  + " + fieldValueFromNode2);
            }
        }

        return diffsList;
    }

    public static List<String> getDiff(JsonNode node1, JsonNode node2) {

        List<String> diffsList = new ArrayList<>();

        Set<String> fields = Parser.getFields(node1);
        fields.addAll(Parser.getFields(node2));
        List<String> sortedFields = new ArrayList<>(fields);
        Collections.sort(sortedFields);

        for (String field : sortedFields) {

            if (node1.has(field)) {
                JsonNode valueNode = node1.get(field);
                if (node2.has(field)) {
                    if (node2.get(field).equals(valueNode)) {
                        diffsList.add(stylishFormatter(field, valueNode, DiffStatus.EQUAL));
                    } else {
                        diffsList.add(stylishFormatter(field, valueNode, DiffStatus.REMOVED));
                        diffsList.add(stylishFormatter(field, node2.get(field), DiffStatus.ADDED));
                    }
                } else {
                    diffsList.add(stylishFormatter(field, valueNode, DiffStatus.REMOVED));
                }
            } else {
                diffsList.add(stylishFormatter(field, node2.get(field), DiffStatus.ADDED));
            }
        }

        return diffsList;
    }

    private static String stylishFormatter(String field, JsonNode node, DiffStatus status) {
        String statusString;

        switch (status) {
            case ADDED:
                statusString = "  + ";
                break;
            case REMOVED:
                statusString = "  - ";
                break;
            default:
                statusString = "    ";
                break;

        }

        String value = node.toString();
        value = value.replaceAll("\"", "");
        if (node.isContainerNode()) {
            value = value.replaceAll(",", ", ");
            value = value.replaceAll(":", "=");
        }

        return statusString + field + ": " + value;
    }


    public static String diffsListToString(List<String> diffsList) {
        StringBuilder sb = new StringBuilder("{\n");
        for (String diff : diffsList) {
            sb.append(diff + "\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
