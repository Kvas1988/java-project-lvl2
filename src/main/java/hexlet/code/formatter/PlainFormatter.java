package hexlet.code.formatter;

import hexlet.code.Diff;

import java.util.Collection;
import java.util.Map;

public final class PlainFormatter implements Formatter {

    @Override
    public String formatDiffsList(Map<String, Diff> diffs) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Diff> entry : diffs.entrySet()) {

            String value;
            String field = entry.getKey();
            Diff diff = entry.getValue();

            switch (diff.getStatus()) {
                case ADDED -> {
                    value = formatValue(diff.getValue2());
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
                    value = formatValue(diff.getValue1());
                    String modifiedValue = formatValue(diff.getValue2());
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

    private String formatValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Collection<?> || value instanceof Map<?, ?>) {
            return  "[complex value]";
        }

        if (value instanceof String) {
            String result = "'" + value + "'";
            return result.replaceAll("\"", "'");
        }

        return value.toString();
    }
}
