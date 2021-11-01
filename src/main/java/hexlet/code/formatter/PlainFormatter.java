package hexlet.code.formatter;

import com.fasterxml.jackson.databind.JsonNode;
import hexlet.code.Diff;

import java.util.List;

public final class PlainFormatter implements Formatter {

    @Override
    public String formatDiffsList(List<Diff> diffList) {
        StringBuilder sb = new StringBuilder();

        // TODO: Remove new line in the end
        boolean isFirstLine = true;

        for (Diff diff : diffList) {

            if (isFirstLine) {
                isFirstLine = false;
            } else {
                sb.append("\n");
            }

            String value;
            String field = diff.getField();

            switch (diff.getStatus()) {
                case EQUAL:
                    break;
                case ADDED:
                    value = formatValue(diff.getModifiedValue());
                    sb.append("Property '")
                            .append(field)
                            .append("' was added with value: ")
                            .append(value)
                    break;
                case REMOVED:
                    sb.append("Property '")
                            .append(field)
                            .append("' was removed")
                    break;
                case MODIFIED:
                    value = formatValue(diff.getInitValue());
                    String modifiedValue = formatValue(diff.getModifiedValue());
                    sb.append("Property '")
                            .append(field)
                            .append("' was updated. From ")
                            .append(value)
                            .append(" to ")
                            .append(modifiedValue)
                    break;
                default:
                    break;
            }
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
