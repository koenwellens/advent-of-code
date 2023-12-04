package day3;

public final class TheSymbol implements Symbol {

    private final String value;
    private final int row;
    private final int column;

    public TheSymbol(final String value, final int row, final int column) {
        this.value = value;
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean isAdjacentTo(final int row, final int column) {
        final var rowDiff = Math.abs(this.row - row);
        final var columnDiff = Math.abs(this.column - column);
        return rowDiff <= 1 && columnDiff <= 1;
    }
}
