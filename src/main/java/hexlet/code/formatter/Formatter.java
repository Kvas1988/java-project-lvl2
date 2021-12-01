package hexlet.code.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import hexlet.code.Diff;

import java.util.Map;

public interface Formatter {

    static Formatter getFormatter(String format) {
        Formatter formatter = switch (format) {
            case "plain" -> new PlainFormatter();
            case "json" -> new JsonFormatter();
            case "stylish" -> new StylishFormatter();
            default -> throw new RuntimeException("invalid format type given");
        };

        return formatter;
    }

    String formatDiffsList(Map<String, Diff> diffList) throws JsonProcessingException;
}
