package hexlet.code.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Diff;

import java.util.List;

public final class JsonFormatter implements Formatter {
    @Override
    public String formatDiffsList(List<Diff> diffList) {

        StringBuilder sb = new StringBuilder();

        ObjectMapper mapper = new ObjectMapper();

        for (Diff diff : diffList) {
            try {
                sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(diff));
            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
            }
        }

        return sb.toString();
    }
}
