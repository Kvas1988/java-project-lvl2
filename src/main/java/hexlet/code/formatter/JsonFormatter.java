package hexlet.code.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Diff;

import java.util.List;

public final class JsonFormatter implements Formatter {
    @Override
    public String formatDiffsList(List<Diff> diffs) {

        StringBuilder sb = new StringBuilder();
        ObjectMapper mapper = new ObjectMapper();

        for (Diff diff : diffs) {
            try {
                String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(diff);
                output = output.replaceAll("\"null\"", "null");
                sb.append(output);
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
            }
        }

        return sb.toString();
    }
}
