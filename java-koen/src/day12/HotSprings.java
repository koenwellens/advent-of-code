package day12;

import common.AbstractObjectBasedOnInput;

import java.util.List;

public final class HotSprings extends AbstractObjectBasedOnInput<Long> {
    public HotSprings(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Long run() {
        return textLines.stream()
                .map(SimpleSpringRow::new)
                .mapToLong(Row::possibleArrangements)
                .sum();
    }

    @Override
    public Long alternateRun() {
        return textLines.stream()
                .map(ImprovedSpringRow::new)
                .mapToLong(Row::possibleArrangements)
                .sum();
    }
}
