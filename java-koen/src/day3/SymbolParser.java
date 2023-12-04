package day3;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class SymbolParser implements Parser<Collection<Symbol>> {

    private final String line;
    private final int row;

    public SymbolParser(final String line, final int row) {
        this.line = line;
        this.row = row;
    }

    @Override
    public Collection<Symbol> value() {
        final var characters = line.split("");

        return IntStream.range(0, characters.length)
                .filter(i -> !".".equals(characters[i]))
                .filter(i -> !new IsNumber().test(characters[i]))
                .mapToObj(i -> new TheSymbol(characters[i], row, i))
                .collect(Collectors.toUnmodifiableList());
    }
}
