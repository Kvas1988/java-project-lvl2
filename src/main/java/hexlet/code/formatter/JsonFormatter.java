package hexlet.code.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Diff;

import java.util.Map;

public final class JsonFormatter implements Formatter {

    @Override
    public String formatDiffsList(Map<String, Diff> diffs) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(diffs);
    }
}
