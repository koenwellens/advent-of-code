package day9;

import common.AbstractObjectBasedOnInput;

import java.util.List;

public final class OASISReport extends AbstractObjectBasedOnInput<Long> {
    public OASISReport(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Long run() {
        return textLines.stream()
                .map(TheHistory::new)
                .mapToLong(History::nextValue)
                .sum();
    }

    @Override
    public Long alternateRun() {
        return textLines.stream()
                .map(TheHistory::new)
                .mapToLong(History::previousValue)
                .sum();
    }
}
