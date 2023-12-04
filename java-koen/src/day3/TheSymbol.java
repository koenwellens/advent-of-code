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
        return this.row == row && this.column == column;
    }

    @Override
    public boolean isPotentialGear() {
        return "*".equals(this.value);
    }
}
