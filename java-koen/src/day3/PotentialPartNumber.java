package day3;

import java.util.Collection;

import static java.lang.Integer.parseInt;
import static java.util.stream.IntStream.range;

public final class PotentialPartNumber implements PartNumber {

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
        return range(this.startColumn - 1, this.startColumn + this.number.length() + 1)
                .anyMatch(column -> symbol.isAdjacentTo(this.row - 1, column))
                || symbol.isAdjacentTo(this.row, this.startColumn - 1)
                || symbol.isAdjacentTo(this.row, this.startColumn + this.number.length())
                || range(this.startColumn - 1, this.startColumn + this.number.length() + 1)
                .anyMatch(column -> symbol.isAdjacentTo(this.row + 1, column));
    }
}
