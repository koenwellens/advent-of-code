package day3;

import java.util.Collection;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

public final class PotentialPartNumber implements PartNumber, Symbol {

    private final String number;
    private final int row;
    private final int startColumn;

    public PotentialPartNumber(final String number, final int row, final int startColumn) {
        this.number = number;
        this.row = row;
        this.startColumn = startColumn;
    }

    @Override
    public int value() {
        return parseInt(this.number);
    }

    @Override
    public boolean isAdjacentToOneOf(final Collection<Symbol> symbols) {
        return symbols.stream().anyMatch(this::isAdjacentTo);
    }

    @Override
    public boolean isAdjacentTo(final Symbol symbol) {
        return !equals(symbol) && range(this.startColumn, this.startColumn + this.number.length() + 1)
                .anyMatch(column -> symbol.isAdjacentTo(this.row, column));
    }

    @Override
    public boolean isAdjacentTo(final int row, final int column) {
        final var rowDiff = Math.abs(this.row - row);
        return rowDiff <= 1 && range(this.startColumn, this.number.length() + 1).anyMatch(myColumn -> Math.abs(myColumn - column) <= 1);
    }
}
