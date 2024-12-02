package day17;

import common.Parser;

import java.util.List;

public final class EndCoordinatesParser implements Parser<String> {
    private final List<String> textLines;

    public EndCoordinatesParser(final List<String> textLines) {
        this.textLines = textLines;
    }

    @Override
    public String value() {
        final var maxY = textLines.size() - 1;
        return STR."\{textLines.get(maxY).length() - 1};\{maxY}";
    }
}
