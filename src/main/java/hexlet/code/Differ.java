package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import hexlet.code.formatter.Formatter;
import hexlet.code.formatter.StylishFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Differ {

    public static String generate(String filePath1, String filePath2, String format) throws IOException {
        List<Diff> diffsList = generateDiffListFromTwoFilePaths(filePath1, filePath2);

        Formatter formatter = Formatter.getFormatter(format);
        return formatter.formatDiffsList(diffsList);
    }

    public static String generate(String filePath1, String filePath2) throws IOException {
        List<Diff> diffsList = generateDiffListFromTwoFilePaths(filePath1, filePath2);
        Formatter formatter = new StylishFormatter();
        return formatter.formatDiffsList(diffsList);
    }


    public static List<Diff> generateDiffListFromTwoFilePaths(String filePath1, String filePath2) throws IOException {
        // Parse files to JsonNodes
        File file1 = getFileObj(filePath1);
        JsonNode dataFile1 = Parser.getNodeDataFromFile(file1);

        File file2 = getFileObj(filePath2);
        JsonNode dataFile2 = Parser.getNodeDataFromFile(file2);

        return getDiff(dataFile1, dataFile2);
    }


    public static File getFileObj(String filePath) {
        Path path = Paths.get(filePath);
        return path.toAbsolutePath().toFile();
    }


    public static List<Diff> getDiff(JsonNode node1, JsonNode node2) {

        List<Diff> diffsList = new ArrayList<>();

        // get fields from nodes and sort them
        Set<String> fields = Parser.getFields(node1);
        fields.addAll(Parser.getFields(node2));
        List<String> sortedFields = new ArrayList<>(fields);
        Collections.sort(sortedFields);

        for (String field : sortedFields) {

            if (node1.has(field)) {
                JsonNode valueNode = node1.get(field);
                if (node2.has(field)) {
                    if (node2.get(field).equals(valueNode)) {
                        diffsList.add(new Diff(field, valueNode, valueNode, Diff.DiffStatus.EQUAL));
                    } else {
                        diffsList.add(new Diff(field, valueNode, node2.get(field), Diff.DiffStatus.MODIFIED));
                    }
                } else {
                    diffsList.add(new Diff(field, valueNode, null, Diff.DiffStatus.REMOVED));
                }
            } else {
                diffsList.add(new Diff(field, null, node2.get(field), Diff.DiffStatus.ADDED));
            }
        }

        return diffsList;
    }
}
