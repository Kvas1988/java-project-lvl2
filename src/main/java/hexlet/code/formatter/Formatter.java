package hexlet.code.formatter;

import hexlet.code.Diff;

import java.util.List;

public interface Formatter {

    static Formatter getFormatter(String format) {
        if (format.equals("plain")) {
            return new PlainFormatter();
        }

        return new StylishFormatter();
    }

    String formatDiffsList(List<Diff> diffList);
}
