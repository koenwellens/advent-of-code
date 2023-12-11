package day9;

import common.Parser;

import static java.util.Arrays.stream;

public final class SequenceParser implements Parser<Sequence> {

    private final String line;

    public SequenceParser(final String line) {
        this.line = line;
    }

    @Override
    public Sequence value() {
        final var numbers = stream(line.split(" "))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();

        return new TheSequence(numbers);
    }
}
