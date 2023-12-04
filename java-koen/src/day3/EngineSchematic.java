package day3;

import common.AbstractObjectBasedOnInput;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public final class EngineSchematic extends AbstractObjectBasedOnInput<Integer> {
    public EngineSchematic(final List<String> textLines) {
        super(textLines);
    }

    @Override
    public Integer run() {
        final var symbols = new SymbolListParser(this.textLines).value();

        return actualPartNumbers(symbols).stream()
                .mapToInt(PartNumber::value)
                .sum();
    }

    private Collection<PartNumber> actualPartNumbers(Collection<Symbol> symbols) {
        final var potentialPartNumbers = new PartNumberListParser(this.textLines).value();

        return potentialPartNumbers.stream()
                .filter(ppn -> ppn.isAdjacentToOneOf(symbols))
                .collect(toUnmodifiableList());
    }

    @Override
    public Integer alternateRun() {
        final var symbols = new SymbolListParser(this.textLines).value();
        final var partNumbers = actualPartNumbers(symbols);

        return symbols.stream().filter(Symbol::isPotentialGear)
                .map(s -> partNumbers.stream().filter(pn -> pn.isAdjacentTo(s)).toList())
                .filter(pn -> pn.size() == 2)
                .map(TheGear::new)
                .mapToInt(Gear::gearRatio)
                .sum();
    }
}
