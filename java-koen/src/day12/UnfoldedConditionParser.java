package day12;

import common.Parser;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public final class UnfoldedConditionParser implements Parser<List<Condition>> {

    private final String line;
    private final int timesToUnfold;

    public UnfoldedConditionParser(final String line, final int timesToUnfold) {
        this.line = line;
        this.timesToUnfold = timesToUnfold;
    }

    @Override
    public List<Condition> value() {
        final var conditions = line.split(" ")[0].trim();
        final var unfoldedConditions = IntStream.range(0, timesToUnfold).mapToObj(i -> conditions).collect(joining("?"));

        return stream(unfoldedConditions.split(""))
                .map(Condition::fromSymbol)
                .toList();
    }
}
