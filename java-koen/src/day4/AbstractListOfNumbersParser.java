package day4;

import java.util.Collection;
import java.util.function.Function;

import static java.util.Arrays.stream;

public abstract class AbstractListOfNumbersParser<T> implements Parser<Collection<T>> {

    private final String line;

    public AbstractListOfNumbersParser(final String line) {
        this.line = line;
    }

    protected abstract String partOfTheNumbersArray(final String[] numbersArray);

    protected abstract Function<String, T> constructor();

    public Collection<T> value() {
        final var cardSplit = this.line.split(": ");
        final var numbersSplit = cardSplit[1].split(" \\D ");

        return stream(partOfTheNumbersArray(numbersSplit).split(" "))
                .filter(s -> !s.isBlank())
                .filter(s -> !s.isEmpty())
                .map(constructor())
                .toList();
    }
}
