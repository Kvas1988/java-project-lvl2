package hexlet.code;

import hexlet.code.formatter.Formatter;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Differ {

    public static String generate(String filePath1, String filePath2, String format) throws IOException {
        List<Diff> diffs = buildDiff(filePath1, filePath2);

        Formatter formatter = Formatter.getFormatter(format);
        return formatter.formatDiffsList(diffs);
    }

    public static String generate(String filePath1, String filePath2) throws IOException {
        return generate(filePath1, filePath2, "stylish");
    }

    public static List<Diff> buildDiff(String filePath1, String filePath2) throws IOException {
        Map<String, Object> dataFile1 = Parser.parse(readFile(filePath1), getFileType(filePath1));
        Map<String, Object> dataFile2 = Parser.parse(readFile(filePath2), getFileType(filePath2));

        return getDiff(dataFile1, dataFile2);
    }

    public static String readFile(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath).toAbsolutePath());
    }

    public static String getFileType(String filePath) {
        return FilenameUtils.getExtension(filePath);
    }


    public static List<Diff> getDiff(Map<String, Object> node1, Map<String, Object> node2) {

        List<Diff> diffs = new ArrayList<>();

        // get fields from nodes and sort them
        Set<String> keys = new HashSet<>();
        keys.addAll(node1.keySet());
        keys.addAll(node2.keySet());
        List<String> sortedKeys = new ArrayList<>(keys);
        Collections.sort(sortedKeys);

        for (String key : sortedKeys) {

            if (node1.containsKey(key)) {
                Object valueNode1 = node1.get(key) == null ? "null" : node1.get(key);
                if (node2.containsKey(key)) {
                    Object valueNode2 = node2.get(key) == null ? "null" : node2.get(key);
                    if (valueNode2.equals(valueNode1)) {
                        diffs.add(new Diff(key, valueNode1, valueNode2, Diff.DiffStatus.EQUAL));
                    } else {
                        diffs.add(new Diff(key, valueNode1, valueNode2, Diff.DiffStatus.MODIFIED));
                    }
                } else {
                    diffs.add(new Diff(key, valueNode1, "null", Diff.DiffStatus.REMOVED));
                }
            } else {
                Object valueNode2 = node2.get(key) == null ? "null" : node2.get(key);
                diffs.add(new Diff(key, "null", valueNode2, Diff.DiffStatus.ADDED));
            }
        }

        return diffs;
    }

}
