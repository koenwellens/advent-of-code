package day12;

import common.Parser;

import java.util.List;

import static java.util.Arrays.stream;

public final class ConditionParser implements Parser<List<Condition>> {

    private final String line;

    public ConditionParser(final String line) {
        this.line = line;
    }

    @Override
    public List<Condition> value() {
        final var conditions = line.split(" ")[0].trim();

        return stream(conditions.split(""))
                .map(Condition::fromSymbol)
                .toList();
    }
}
