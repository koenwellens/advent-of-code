package day12;

import static java.util.Arrays.stream;

public enum Condition {

    OPERATIONAL("."),
    DAMAGED("#"),
    UNKNOWN("?"),
    ;

    private final String symbol;

    Condition(final String symbol) {
        this.symbol = symbol;
    }

    public static Condition fromSymbol(String symbol) {
        return stream(values()).filter(c -> symbol.equals(c.symbol)).findFirst().orElseThrow();
    }

    @Override
    public String toString() {
        return symbol;
    }
}
