package hexlet.code.formatter;

import hexlet.code.Diff;

import java.util.List;

public final class StylishFormatter implements Formatter {

    @Override
    public String formatDiffsList(List<Diff> diffList) {

        StringBuilder sb = new StringBuilder("{\n");

        for (Diff diff : diffList) {
            String field = diff.getField();
            String statusString;
            String value;

            switch (diff.getStatus()) {
                case ADDED -> {
                    statusString = "  + ";
                    value = diff.getModifiedValue().toString();
                    sb.append(formatDiff(field, value, statusString));
                }
                case REMOVED -> {
                    statusString = "  - ";
                    value = diff.getInitValue().toString();
                    sb.append(formatDiff(field, value, statusString));
                }
                case MODIFIED -> {
                    statusString = "  - ";
                    value = diff.getInitValue().toString();
                    sb.append(formatDiff(field, value, statusString));

                    statusString = "  + ";
                    value = diff.getModifiedValue().toString();
                    sb.append(formatDiff(field, value, statusString));
                }
                case EQUAL -> {
                    statusString = "    ";
                    value = diff.getModifiedValue().toString();
                    sb.append(formatDiff(field, value, statusString));
                }
                default -> { }
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private String formatDiff(String field, String value, String status) {
        StringBuilder sb = new StringBuilder(status);

        value = value.replaceAll("\"", "");
        sb.append(field)
                .append(": ")
                .append(value)
                .append("\n");

        return sb.toString();
    }
}
