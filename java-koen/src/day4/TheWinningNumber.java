package day4;

import static java.lang.Integer.parseInt;

public final class TheWinningNumber implements WinningNumber {

    private final int number;

    public TheWinningNumber(final String number) {
        this(parseInt(number.trim()));
    }

    private TheWinningNumber(final int number) {
        this.number = number;
    }

    @Override
    public boolean is(final int number) {
        return this.number == number;
    }
}
