package day3;

import common.AbstractObjectBasedOnInput;

import java.util.List;
import java.util.stream.Stream;

public final class EngineSchematic extends AbstractObjectBasedOnInput<Integer> {
    public EngineSchematic(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Integer run() {
        final var symbols = new SymbolListParser(this.textLines).value();
        final var potentialPartNumbers = new PartNumberListParser(this.textLines).value();

        return potentialPartNumbers.stream()
                .filter(ppn -> ppn.isAdjacentToOneOf(Stream.concat(symbols.stream(), potentialPartNumbers.stream()).toList()))
                .mapToInt(PartNumber::value)
                .sum();
    }

    @Override
    public Integer alternateRun() {
        return null;
    }
}
