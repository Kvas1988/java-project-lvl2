package hexlet.code.formatter;

import hexlet.code.Diff;

import java.util.Map;

public final class StylishFormatter implements Formatter {

    @Override
    public String formatDiffsList(Map<String, Diff> diffs) {

        StringBuilder sb = new StringBuilder("{\n");

        for (Map.Entry<String, Diff> entry : diffs.entrySet()) {
            String field = entry.getKey();
            String statusString;
            Diff diff = entry.getValue();
            String value;

            switch (diff.getStatus()) {
                case ADDED -> {
                    statusString = "  + ";
                    sb.append(formatDiff(field, diff.getValue2(), statusString));
                }
                case REMOVED -> {
                    statusString = "  - ";
                    sb.append(formatDiff(field, diff.getValue1(), statusString));
                }
                case MODIFIED -> {
                    statusString = "  - ";
                    sb.append(formatDiff(field, diff.getValue1(), statusString));

                    statusString = "  + ";
                    sb.append(formatDiff(field, diff.getValue2(), statusString));
                }
                case EQUAL -> {
                    statusString = "    ";
                    sb.append(formatDiff(field, diff.getValue2(), statusString));

                }
                default -> { }
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private String formatDiff(String field, Object value, String status) {
        StringBuilder sb = new StringBuilder(status);

        sb.append(field)
                .append(": ")
                .append(value)
                .append("\n");

        return sb.toString();
    }

}
