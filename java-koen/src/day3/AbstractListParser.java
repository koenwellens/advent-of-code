package day3;

import common.Parser;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public abstract class AbstractListParser<T, S extends Parser<Collection<T>>> implements Parser<Collection<T>> {

    private final List<String> textLines;

    protected AbstractListParser(final List<String> textLines) {
        this.textLines = textLines;
    }

    protected abstract S parser(String line, int row);

    @Override
    public Collection<T> value() {
        return IntStream.range(0, textLines.size())
                .mapToObj(row -> parser(textLines.get(row), row))
                .map(Parser::value)
                .flatMap(Collection::stream)
                .toList();
    }
}
