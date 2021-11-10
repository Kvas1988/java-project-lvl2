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
                    sb.append(formatDiff(field, value, statusString, diff.getModifiedValue().isContainerNode()));
                }
                case REMOVED -> {
                    statusString = "  - ";
                    value = diff.getInitValue().toString();
                    sb.append(formatDiff(field, value, statusString, diff.getInitValue().isContainerNode()));
                }
                case MODIFIED -> {
                    statusString = "  - ";
                    value = diff.getInitValue().toString();
                    sb.append(formatDiff(field, value, statusString, diff.getInitValue().isContainerNode()));

                    statusString = "  + ";
                    value = diff.getModifiedValue().toString();
                    sb.append(formatDiff(field, value, statusString, diff.getModifiedValue().isContainerNode()));
                }
                case EQUAL -> {
                    statusString = "    ";
                    value = diff.getModifiedValue().toString();
                    sb.append(formatDiff(field, value, statusString, diff.getModifiedValue().isContainerNode()));
                }
                default -> {}
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private String formatDiff(String field, String value, String status, boolean isValueCointaier) {
        StringBuilder sb = new StringBuilder(status);

        value = value.replaceAll("\"", "");
        if (isValueCointaier) {
            value = value.replaceAll(",", ", ");
            value = value.replaceAll(":", "=");
        }
        sb.append(field)
                .append(": ")
                .append(value)
                .append("\n");

        return sb.toString();
    }
}
