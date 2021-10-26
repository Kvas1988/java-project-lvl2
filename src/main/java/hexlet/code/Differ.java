package hexlet.code;

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

    public static String generate(String filePath1, String filePath2) throws IOException {
        // Parse files to JsonNodes
        File file1 = getFileObj(filePath1);
        Map<String, String> dataFile1 = Parser.getDataFromFile(file1);

        File file2 = getFileObj(filePath2);
        Map<String, String> dataFile2 = Parser.getDataFromFile(file2);

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



    public static String diffsListToString(List<String> diffsList) {
        StringBuilder sb = new StringBuilder("{\n");
        for (String diff : diffsList) {
            sb.append(diff + "\n");
        }

        sb.append("}");
        return sb.toString();
    }
}
