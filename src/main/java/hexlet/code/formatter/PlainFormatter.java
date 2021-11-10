package hexlet.code.formatter;

import com.fasterxml.jackson.databind.JsonNode;
import hexlet.code.Diff;

import java.util.List;

public final class PlainFormatter implements Formatter {

    @Override
    public String formatDiffsList(List<Diff> diffList) {
        StringBuilder sb = new StringBuilder();

        for (Diff diff : diffList) {

            String value;
            String field = diff.getField();

            switch (diff.getStatus()) {
                case ADDED -> {
                    value = formatValue(diff.getModifiedValue());
                    sb.append("Property '")
                            .append(field)
                            .append("' was added with value: ")
                            .append(value)
                            .append("\n");
                }
                case REMOVED -> {
                    sb.append("Property '")
                            .append(field)
                            .append("' was removed")
                            .append("\n");
                }
                case MODIFIED -> {
                    value = formatValue(diff.getInitValue());
                    String modifiedValue = formatValue(diff.getModifiedValue());
                    sb.append("Property '")
                            .append(field)
                            .append("' was updated. From ")
                            .append(value)
                            .append(" to ")
                            .append(modifiedValue)
                            .append("\n");
                }
                default -> { }
            }
        }

        // remove last empty line
        int lastLine = sb.lastIndexOf("\n");
        if (lastLine >= 0) {
            sb.delete(lastLine, sb.length());
        }

        return sb.toString();
    }

    private String formatValue(JsonNode value) {
        if (value.isContainerNode()) {
            return  "[complex value]";
        }

        if (value.isNull() || value.isNumber() || value.isBoolean()) {
            return value.toString();
        }

        return value.toString().replaceAll("\"", "'");
    }
}
